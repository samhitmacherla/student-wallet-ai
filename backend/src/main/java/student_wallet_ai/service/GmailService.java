package student_wallet_ai.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import student_wallet_ai.model.Transaction;
import student_wallet_ai.repository.TransactionRepository;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GmailService {

    private static final String APPLICATION_NAME = "Student Wallet AI";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private Gmail getGmailService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Runs every 5 minutes automatically
    @Scheduled(fixedDelay = 300000)
    public void readBankSmsFromGmail() {
        try {
            Gmail service = getGmailService();
            ListMessagesResponse response = service.users().messages()
                    .list("me")
                    .setQ("subject:SMS from AND (UPI OR debited OR credited) newer_than:1d")
                    .execute();

            List<Message> messages = response.getMessages();
            if (messages == null) return;

            for (Message msg : messages) {
                Message fullMsg = service.users().messages().get("me", msg.getId()).execute();
                String body = getEmailBody(fullMsg);
                if (body != null && !body.isEmpty()) {
                    parseAndSaveTransaction(body);
                }
            }
        } catch (Exception e) {
            System.out.println("Gmail read error: " + e.getMessage());
        }
    }

    private String getEmailBody(Message message) {
        try {
            MessagePart payload = message.getPayload();
            if (payload.getBody().getData() != null) {
                return new String(Base64.getUrlDecoder().decode(payload.getBody().getData()));
            }
            if (payload.getParts() != null) {
                for (MessagePart part : payload.getParts()) {
                    if (part.getBody().getData() != null) {
                        return new String(Base64.getUrlDecoder().decode(part.getBody().getData()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading email body: " + e.getMessage());
        }
        return null;
    }

    private void parseAndSaveTransaction(String smsText) {
        try {
            // Pattern to extract amount from SMS
            Pattern amountPattern = Pattern.compile(
                "(?:Rs\\.?|INR|₹)\\s*([\\d,]+\\.?\\d*)",
                Pattern.CASE_INSENSITIVE
            );

            // Pattern to extract merchant
            Pattern merchantPattern = Pattern.compile(
                "(?:to|at)\\s+([A-Za-z0-9\\s]+?)(?:\\s+on|\\s+Ref|\\.|$)",
                Pattern.CASE_INSENSITIVE
            );

            Matcher amountMatcher = amountPattern.matcher(smsText);
            Matcher merchantMatcher = merchantPattern.matcher(smsText);

            if (amountMatcher.find()) {
                String amountStr = amountMatcher.group(1).replace(",", "");
                Double amount = Double.parseDouble(amountStr);

                String merchant = "Unknown";
                if (merchantMatcher.find()) {
                    merchant = merchantMatcher.group(1).trim();
                }

                // Check if debit transaction
                if (smsText.toLowerCase().contains("debited") ||
                    smsText.toLowerCase().contains("debit") ||
                    smsText.toLowerCase().contains("paid")) {

                    Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    transaction.setMerchant(merchant);
                    transaction.setDescription("Auto imported from SMS");
                    transaction.setDate(LocalDateTime.now());
                    transaction.setCategory("Uncategorized");

                    transactionService.saveTransaction(transaction);
                    System.out.println("Auto saved transaction: " + amount + " to " + merchant);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing SMS: " + e.getMessage());
        }
    }
}
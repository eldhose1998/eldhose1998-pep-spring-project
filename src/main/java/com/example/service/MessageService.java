package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;


    /**
     * Creates a new message.
     *
     * @param postedBy        The ID of the user posting the message.
     * @param messageText     The text of the message.
     * @param timePostedEpoch The epoch time when the message was posted.
     * @return An Optional containing the created Message if successful, or an empty Optional if not.
     */

    public Optional<Message> createMessage(int postedBy, String messageText,
                                           long timePostedEpoch) {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return Optional.empty();
        }

        if (!accountRepository.existsById(postedBy)) {
            return Optional.empty();
        }


        Message newMessage = new Message(postedBy, messageText, timePostedEpoch);
        Message savedMessage = messageRepository.save(newMessage);
        return Optional.of(savedMessage);
    }

    /**
     * Retrieves all messages.
     *
     * @return A list of all messages.
     */

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param messageId The ID of the message to retrieve.
     * @return An Optional containing the Message if found, or an empty Optional if not.
     */
    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * Deletes a message by its ID.
     *
     * @param messageId The ID of the message to delete.
     * @return True if the message was successfully deleted, false otherwise.
     */
    public boolean deleteMessageById(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    /**
     * Updates the text of a message identified by its ID.
     *
     * @param messageId The ID of the message to update.
     * @param newText   The new text for the message.
     * @return An Optional containing the updated Message if successful, or an empty Optional if not.
     */
    public Optional<Message> updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return Optional.empty();
        }

        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessageText(newText);
            Message updatedMessage = messageRepository.save(message);
            return Optional.of(updatedMessage);
        }
        return Optional.empty();
    }

    /**
     * Retrieves all messages posted by a specific user.
     *
     * @param userId The ID of the user whose messages to retrieve.
     * @return A list of messages posted by the specified user.
     */
    public List<Message> getMessagesByUserId(int userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
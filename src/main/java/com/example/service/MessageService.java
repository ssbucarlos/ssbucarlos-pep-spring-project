package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.*;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public List<Message> findAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> findMessageById(int id) {
        return messageRepository.findById(id);
    }

    public Message createMessage(Message transientMessage) throws BadMessageException{
        if (transientMessage.getMessageText().isBlank() 
            || transientMessage.getMessageText().length() > 255
            || messageRepository.findById(transientMessage.getPostedBy()).isEmpty()){
            throw new BadMessageException("Invalid Message");
        }
        return messageRepository.save(transientMessage);
    }

    public boolean deleteMessageById(int id){
        if (this.findMessageById(id).isEmpty()){
            return false;
        }
        messageRepository.deleteById(id);
        return true;
    }

    public boolean updateMessageById(int id, Message transientMessage){
        Message existingMessage = messageRepository.findById(id).orElse(null);
        if (existingMessage == null
            || transientMessage.getMessageText().isBlank() 
            || transientMessage.getMessageText().length() > 255
            ){
            return false;
        }
        existingMessage.setMessageText(transientMessage.getMessageText());
        messageRepository.save(existingMessage);
        return true;
    }

    public List<Message> getMessagesFromUser(int accountId){
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}

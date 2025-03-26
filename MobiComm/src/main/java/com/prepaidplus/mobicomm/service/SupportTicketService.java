package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.SupportTicket;
import com.prepaidplus.mobicomm.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;

    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }

    public Optional<SupportTicket> getTicketById(Integer id) {
        return supportTicketRepository.findById(id);
    }

    public List<SupportTicket> getTicketsByUserId(Integer userId) {
        return supportTicketRepository.findByUser_UserId(userId);
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        return supportTicketRepository.save(ticket);
    }

    public SupportTicket updateTicket(Integer id, SupportTicket ticketDetails) {
        return supportTicketRepository.findById(id).map(ticket -> {
            ticket.setIssueType(ticketDetails.getIssueType());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setStatus(ticketDetails.getStatus());
            ticket.setUpdatedAt(ticketDetails.getUpdatedAt());
            return supportTicketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public void deleteTicket(Integer id) {
        supportTicketRepository.deleteById(id);
    }
}
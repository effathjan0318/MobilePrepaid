package com.prepaidplus.mobicomm.controller;

import com.prepaidplus.mobicomm.model.SupportTicket;
import com.prepaidplus.mobicomm.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @GetMapping
    public List<SupportTicket> getAllTickets() {
        return supportTicketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable Integer id) {
        Optional<SupportTicket> ticket = supportTicketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<SupportTicket> getTicketsByUserId(@PathVariable Integer userId) {
        return supportTicketService.getTicketsByUserId(userId);
    }

    @PostMapping
    public SupportTicket createTicket(@RequestBody SupportTicket ticket) {
        return supportTicketService.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicket> updateTicket(@PathVariable Integer id, @RequestBody SupportTicket ticketDetails) {
        try {
            SupportTicket updatedTicket = supportTicketService.updateTicket(id, ticketDetails);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        supportTicketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}

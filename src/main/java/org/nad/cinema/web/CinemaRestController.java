package org.nad.cinema.web;



import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.nad.cinema.dao.FilmRepository;
import org.nad.cinema.dao.TicketRepository;
import org.nad.cinema.entities.Film;
import org.nad.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;




@RestController
@CrossOrigin("*")
public class CinemaRestController {
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private TicketRepository ticketRepository;
	 @GetMapping(path = "/imageFilm/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	    public byte[] image(@PathVariable("id") Long id) throws Exception {
	        Film p = filmRepository.findById(id).get();
	        return Files.readAllBytes(Paths.get(System.getProperty("user.home")+"/cinema/images/"+p.getPhoto()));
	}
	 
	 @PostMapping("/payerTickets")
	 @Transactional
	 public List<Ticket> payerTickets(@RequestBody TicketFrom ticketFrom) {
		 List<Ticket> listTickets=new ArrayList<>();
		 ticketFrom.getTickets().forEach(idTicket->{
			 Ticket ticket=ticketRepository.findById(idTicket).get();
			 ticket.setNomClient(ticketFrom.getNomClient());
			 ticket.setReserve(true);
			 ticketRepository.save(ticket);
			 listTickets.add(ticket);
			 
			 

		 });
		 return listTickets;
	 }
}
@Data
class TicketFrom{
	private String nomClient;
	private int codePayement;
	private List<Long> tickets=new ArrayList<>();
}
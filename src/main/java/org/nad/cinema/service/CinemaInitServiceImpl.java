package org.nad.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;



import org.nad.cinema.dao.CategorieRepository;
import org.nad.cinema.dao.CinemaRepository;
import org.nad.cinema.dao.FilmRepository;
import org.nad.cinema.dao.PlaceRepository;
import org.nad.cinema.dao.ProjectionRepository;
import org.nad.cinema.dao.SalleRepository;
import org.nad.cinema.dao.SeanceRepository;
import org.nad.cinema.dao.TicketRepository;
import org.nad.cinema.dao.VilleRepository;
import org.nad.cinema.entities.Categorie;
import org.nad.cinema.entities.Cinema;
import org.nad.cinema.entities.Film;
import org.nad.cinema.entities.Place;
import org.nad.cinema.entities.Projection;
import org.nad.cinema.entities.Salle;
import org.nad.cinema.entities.Seance;
import org.nad.cinema.entities.Ticket;
import org.nad.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
	
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;

	
	

	
	




	@Override
	public void initVilles() {
		Stream.of("Strasbourg","Paris","Metz","Lyon").forEach(nameVille->{
			Ville ville=new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
			
		});
		
	}

	@Override
	public void initCinemas() {
		
		villeRepository.findAll().forEach(v->{
			Stream.of("Star","Vox","Le Star Saint-Exupery","Trefle")
			.forEach(nameCinema->{
				Cinema cinema=new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle=new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			
			}
			
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place=new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
	   Stream.of("Histoire","Actions","Fiction","Drama").forEach(cat->{
		   Categorie categorie=new Categorie();
		   categorie.setName(cat);
		   categorieRepository.save(categorie);
	   });
		
	}

	@Override
	public void initFilms() {
		double[] duree=new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("12 hommes en colere","Forrest Gump","Green Book","La Ligne verte","Le Parrain","Le Seigneur des anneaux")
		.forEach(titreFilm->{
			Film film=new Film();
			film.setTitre(titreFilm);
			film.setDuree(duree[new Random().nextInt(duree.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices=new double[] {5,6,8,10,15,20};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinema().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
					   Film film=films.get(index);
						seanceRepository.findAll().forEach(seance->{
							Projection projection=new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					
					
				});
			});
		});
		
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket=new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
			
			
		});
		
		
	}

}

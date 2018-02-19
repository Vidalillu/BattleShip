package com.company.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Date;
import java.util.List;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.hibernate.validator.constraints.Email;


@SpringBootApplication
public class salvoApplication {

	public static void main(String[] args) { SpringApplication.run(salvoApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository,
									  GameRepository repository2,
									  GamePlayerRepository repository3,
									  ShipRepository repository4,
									  SalvoRepository repository5,
									  ScoreRepository repository6) {

		return (args) -> {
			// save a couple of customers
			Player player1 = new Player("cobrian","c.obrian@ctu.gov", "42");
			Player player2 = new Player("jbauer", "j.bauer@ctu.gov", "24");
			Player player3 = new Player("kbauer","kim_bauer@gmail.com", "kb");
			Player player4 = new Player("talmeida","t.almeida@ctu.gov", "mole");
			Player player5 = new Player("vidalillu","vidalillu@gmail.com", "2515");

			Game game1 = new Game(new Date());
			Game game2 = new Game(new Date(System.currentTimeMillis() + 3600 * 1000));
			Game game3 = new Game(new Date(System.currentTimeMillis() + 7200 * 1000));
            Game game4 = new Game(new Date(System.currentTimeMillis() + 10800 * 1000));
			Game game5 = new Game(new Date(System.currentTimeMillis() + 14400 * 1000));
			Game game6 = new Game(new Date(System.currentTimeMillis() + 18000 * 1000));
			Game game7 = new Game(new Date(System.currentTimeMillis() + 21600 * 1000));
			Game game8 = new Game(new Date(System.currentTimeMillis() + 25200 * 1000));

			GamePlayer gamePlayer1 = new GamePlayer(player2, game1, new Date());
            GamePlayer gamePlayer2 = new GamePlayer(player1, game1, new Date(System.currentTimeMillis() + 3600 * 1000));
            GamePlayer gamePlayer3 = new GamePlayer(player2, game2, new Date());
            GamePlayer gamePlayer4 = new GamePlayer(player1, game2, new Date(System.currentTimeMillis() + 3600 * 1000));
            GamePlayer gamePlayer5 = new GamePlayer(player1, game3, new Date());
            GamePlayer gamePlayer6 = new GamePlayer(player4, game3, new Date(System.currentTimeMillis() + 3600 * 1000));
            GamePlayer gamePlayer7 = new GamePlayer(player1, game4, new Date());
			GamePlayer gamePlayer8 = new GamePlayer(player2, game4, new Date(System.currentTimeMillis() + 3600 * 1000));
			GamePlayer gamePlayer9 = new GamePlayer(player4, game5, new Date());
			GamePlayer gamePlayer10 = new GamePlayer(player2, game5, new Date(System.currentTimeMillis() + 3600 * 1000));
			GamePlayer gamePlayer11 = new GamePlayer(player3, game6, new Date());
			GamePlayer gamePlayer12 = new GamePlayer(player4, game7, new Date());
			GamePlayer gamePlayer13 = new GamePlayer(player3, game8, new Date());
			GamePlayer gamePlayer14 = new GamePlayer(player4, game8, new Date(System.currentTimeMillis() + 3600 * 1000));

			String ship1 = "carrier";
			String ship2 = "battleship";
			String ship3 = "submarine";
			String ship4 = "destroyer";
			String ship5 = "patrolBoat";

			List<String> shipLocation1 = Arrays.asList("H2", "H3", "H4");
			List<String> shipLocation2 = Arrays.asList("E1", "F1", "G1");
			List<String> shipLocation3 = Arrays.asList("B4", "B5");
			List<String> shipLocation4 = Arrays.asList("B5", "C5", "D5");
			List<String> shipLocation5 = Arrays.asList("F1", "F2");
			List<String> shipLocation6 = Arrays.asList("B5", "C5", "D5");
			List<String> shipLocation7 = Arrays.asList("C6", "C7");
			List<String> shipLocation8 = Arrays.asList("A2", "A3", "A4");
			List<String> shipLocation9 = Arrays.asList("G6", "H6");
			List<String> shipLocation10 = Arrays.asList("B5", "C5", "D5");
			List<String> shipLocation11 = Arrays.asList("C6", "C7");
			List<String> shipLocation12 = Arrays.asList("A2", "A3", "A4");
			List<String> shipLocation13 = Arrays.asList("G6", "H6");
			List<String> shipLocation14 = Arrays.asList("B5", "C5", "D5");
			List<String> shipLocation15 = Arrays.asList("C6", "C7");
            List<String> shipLocation16 = Arrays.asList("A2", "A3", "A4");
            List<String> shipLocation17 = Arrays.asList("G6", "H6");
            List<String> shipLocation18 = Arrays.asList("B5", "C5", "D5");
            List<String> shipLocation19 = Arrays.asList("C6", "C7");
            List<String> shipLocation20 = Arrays.asList("A2", "A3", "A4");
            List<String> shipLocation21 = Arrays.asList("G6", "H6");
            List<String> shipLocation22 = Arrays.asList("B5", "C5", "D5");
            List<String> shipLocation23 = Arrays.asList("C6", "C7");
            List<String> shipLocation24 = Arrays.asList("B5", "C5", "D5");
            List<String> shipLocation25 = Arrays.asList("C6", "C7");
            List<String> shipLocation26 = Arrays.asList("A2", "A3", "A4");
            List<String> shipLocation27 = Arrays.asList("G6", "H6");

            Ship playerShip1 = new Ship(ship4, shipLocation1, gamePlayer1);
			Ship playerShip2 = new Ship(ship3, shipLocation2, gamePlayer1);
			Ship playerShip3 = new Ship(ship5, shipLocation3, gamePlayer1);
			Ship playerShip4 = new Ship(ship4, shipLocation4, gamePlayer2);
			Ship playerShip5 = new Ship(ship5, shipLocation5, gamePlayer2);
			Ship playerShip6 = new Ship(ship4, shipLocation6, gamePlayer3);
			Ship playerShip7 = new Ship(ship5, shipLocation7, gamePlayer3);
			Ship playerShip8 = new Ship(ship3, shipLocation8, gamePlayer4);
			Ship playerShip9 = new Ship(ship5, shipLocation9, gamePlayer4);
			Ship playerShip10 = new Ship(ship4, shipLocation10, gamePlayer5);
			Ship playerShip11 = new Ship(ship5, shipLocation11, gamePlayer5);
			Ship playerShip12 = new Ship(ship3, shipLocation12, gamePlayer6);
			Ship playerShip13 = new Ship(ship5, shipLocation13, gamePlayer6);
			Ship playerShip14 = new Ship(ship4, shipLocation14, gamePlayer7);
			Ship playerShip15 = new Ship(ship5, shipLocation15, gamePlayer7);
            Ship playerShip16 = new Ship(ship3, shipLocation16, gamePlayer8);
            Ship playerShip17 = new Ship(ship5, shipLocation17, gamePlayer8);
            Ship playerShip18 = new Ship(ship4, shipLocation18, gamePlayer9);
            Ship playerShip19 = new Ship(ship5, shipLocation19, gamePlayer9);
            Ship playerShip20 = new Ship(ship3, shipLocation20, gamePlayer10);
            Ship playerShip21 = new Ship(ship5, shipLocation21, gamePlayer10);
            Ship playerShip22 = new Ship(ship4, shipLocation22, gamePlayer11);
            Ship playerShip23 = new Ship(ship5, shipLocation23, gamePlayer11);
            Ship playerShip24 = new Ship(ship4, shipLocation24, gamePlayer13);
            Ship playerShip25 = new Ship(ship5, shipLocation25, gamePlayer13);
            Ship playerShip26 = new Ship(ship3, shipLocation26, gamePlayer14);
            Ship playerShip27 = new Ship(ship5, shipLocation27, gamePlayer14);

			List<String> salvoLocation1 = Arrays.asList("B5", "C5", "F1");
			List<String> salvoLocation2 = Arrays.asList("B4", "B5", "B6");
			List<String> salvoLocation3 = Arrays.asList("F2", "D5");
			List<String> salvoLocation4 = Arrays.asList("E1", "H3", "A2");
			List<String> salvoLocation5 = Arrays.asList("A2", "A4", "G6");
			List<String> salvoLocation6 = Arrays.asList("B5", "D5", "C7");
			List<String> salvoLocation7 = Arrays.asList("A3", "H6");
			List<String> salvoLocation8 = Arrays.asList("C5", "C6");
			List<String> salvoLocation9 = Arrays.asList("G6", "H6", "A4");
			List<String> salvoLocation10 = Arrays.asList("H1", "H2", "H3");
			List<String> salvoLocation11 = Arrays.asList("A2", "A3", "D8");
			List<String> salvoLocation12 = Arrays.asList("E1", "F2", "G3");
			List<String> salvoLocation13 = Arrays.asList("A3", "A4", "F7");
			List<String> salvoLocation14 = Arrays.asList("B5", "C6", "H1");
			List<String> salvoLocation15 = Arrays.asList("A2", "G6", "H6");
			List<String> salvoLocation16 = Arrays.asList("C5", "C7", "D5");
			List<String> salvoLocation17 = Arrays.asList("A1", "A2", "A3");
			List<String> salvoLocation18 = Arrays.asList("B5", "B6", "C7");
			List<String> salvoLocation19 = Arrays.asList("G6", "G7", "G8");
			List<String> salvoLocation20 = Arrays.asList("C6", "D6", "E6");
			List<String> salvoLocation21 = Arrays.asList("H1", "H8");

			Salvo playerSalvo1 = new Salvo(1, salvoLocation1, gamePlayer1);
			Salvo playerSalvo2 = new Salvo(1, salvoLocation2, gamePlayer2);
			Salvo playerSalvo3 = new Salvo(2, salvoLocation3, gamePlayer1);
			Salvo playerSalvo4 = new Salvo(2, salvoLocation4, gamePlayer2);
			Salvo playerSalvo5 = new Salvo(1, salvoLocation5, gamePlayer3);
			Salvo playerSalvo6 = new Salvo(1, salvoLocation6, gamePlayer4);
			Salvo playerSalvo7 = new Salvo(2, salvoLocation7, gamePlayer3);
			Salvo playerSalvo8 = new Salvo(2, salvoLocation8, gamePlayer4);
			Salvo playerSalvo9 = new Salvo(1, salvoLocation9, gamePlayer5);
			Salvo playerSalvo10 = new Salvo(1, salvoLocation10, gamePlayer6);
			Salvo playerSalvo11 = new Salvo(2, salvoLocation11, gamePlayer5);
			Salvo playerSalvo12 = new Salvo(2, salvoLocation12, gamePlayer6);
			Salvo playerSalvo13 = new Salvo(1, salvoLocation13, gamePlayer7);
			Salvo playerSalvo14 = new Salvo(1, salvoLocation14, gamePlayer8);
			Salvo playerSalvo15 = new Salvo(2, salvoLocation15, gamePlayer7);
			Salvo playerSalvo16 = new Salvo(2, salvoLocation16, gamePlayer8);
			Salvo playerSalvo17 = new Salvo(1, salvoLocation17, gamePlayer9);
			Salvo playerSalvo18 = new Salvo(1, salvoLocation18, gamePlayer10);
			Salvo playerSalvo19 = new Salvo(2, salvoLocation19, gamePlayer9);
			Salvo playerSalvo20 = new Salvo(2, salvoLocation20, gamePlayer10);
			Salvo playerSalvo21 = new Salvo(3, salvoLocation21, gamePlayer10);

			Score score1 = new Score(player2, game1, 3,new Date(System.currentTimeMillis() + 86400 * 1000));
			Score score2 = new Score(player1, game1, 0,new Date(System.currentTimeMillis() + 90000 * 1000));
			Score score3 = new Score(player2, game2, 1,new Date(System.currentTimeMillis() + 93600 * 1000));
			Score score4 = new Score(player1, game2, 1,new Date(System.currentTimeMillis() + 97200 * 1000));
			Score score5 = new Score(player1, game3, 3,new Date(System.currentTimeMillis() + 100800 * 1000));
			Score score6 = new Score(player4, game3, 0,new Date(System.currentTimeMillis() + 104400 * 1000));
			Score score7 = new Score(player1, game4, 1,new Date(System.currentTimeMillis() + 108000 * 1000));
			Score score8 = new Score(player2, game4, 1,new Date(System.currentTimeMillis() + 111600 * 1000));


			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);
			repository.save(player5);

			repository2.save(game1);
			repository2.save(game2);
			repository2.save(game3);
			repository2.save(game4);
			repository2.save(game5);
			repository2.save(game6);
			repository2.save(game7);
			repository2.save(game8);

			repository3.save(gamePlayer1);
			repository3.save(gamePlayer2);
			repository3.save(gamePlayer3);
			repository3.save(gamePlayer4);
			repository3.save(gamePlayer5);
			repository3.save(gamePlayer6);
            repository3.save(gamePlayer7);
			repository3.save(gamePlayer8);
			repository3.save(gamePlayer9);
			repository3.save(gamePlayer10);
			repository3.save(gamePlayer11);
			repository3.save(gamePlayer12);
			repository3.save(gamePlayer13);
			repository3.save(gamePlayer14);

			repository4.save(playerShip1);
			repository4.save(playerShip2);
			repository4.save(playerShip3);
			repository4.save(playerShip4);
			repository4.save(playerShip5);
			repository4.save(playerShip6);
			repository4.save(playerShip7);
			repository4.save(playerShip8);
			repository4.save(playerShip9);
			repository4.save(playerShip10);
			repository4.save(playerShip11);
			repository4.save(playerShip12);
			repository4.save(playerShip13);
			repository4.save(playerShip14);
			repository4.save(playerShip15);
            repository4.save(playerShip16);
            repository4.save(playerShip17);
            repository4.save(playerShip18);
            repository4.save(playerShip19);
            repository4.save(playerShip20);
            repository4.save(playerShip21);
            repository4.save(playerShip22);
            repository4.save(playerShip23);
            repository4.save(playerShip24);
            repository4.save(playerShip25);
            repository4.save(playerShip26);
            repository4.save(playerShip27);

            repository5.save(playerSalvo1);
			repository5.save(playerSalvo2);
			repository5.save(playerSalvo3);
			repository5.save(playerSalvo4);
			repository5.save(playerSalvo5);
			repository5.save(playerSalvo6);
			repository5.save(playerSalvo7);
			repository5.save(playerSalvo8);
			repository5.save(playerSalvo9);
			repository5.save(playerSalvo10);
			repository5.save(playerSalvo11);
			repository5.save(playerSalvo12);
			repository5.save(playerSalvo13);
			repository5.save(playerSalvo14);
			repository5.save(playerSalvo15);
			repository5.save(playerSalvo16);
			repository5.save(playerSalvo17);
			repository5.save(playerSalvo18);
			repository5.save(playerSalvo19);
			repository5.save(playerSalvo20);
			repository5.save(playerSalvo21);

			repository6.save(score1);
			repository6.save(score2);
			repository6.save(score3);
			repository6.save(score4);
			repository6.save(score5);
			repository6.save(score6);
			repository6.save(score7);
			repository6.save(score8);
		};

	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
				Player player = playerRepository.findByUserName(userName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + userName);
				}
			}
		};
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/games", "/api/players", "/api/leaderboard", "/web/games.html", "/web/css/**", "/web/js/**", "/web/img/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER")
				.and()

			.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");
		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}

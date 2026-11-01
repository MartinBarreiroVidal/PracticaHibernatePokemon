import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.sql.Date;
import java.util.List;
import java.io.FileWriter;

public class MainParte2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = factory.openSession();

        System.out.println("Secuencia da Parte 2 hecha:");

        //Inserir 10 pokemons e 2 entrenadores
        Transaction tx = session.beginTransaction();
        for (int i = 1; i <= 10; i++) {
            session.save(new Pokedex("Pokemon_P2_" + i, 20.0 + i, "Misc_" + i));
        }
        session.save(new Adestrador("Ash Ketchum", Date.valueOf("1997-04-01")));
        session.save(new Adestrador("Misty", Date.valueOf("1998-05-15")));
        tx.commit();

        listarPokedex(session, "Pokedex tras inserción");
        listarAdestradores(session, "Adestradores tras inserción");

        //Modificar
        tx = session.beginTransaction();
        List<Adestrador> ads = session.createQuery("FROM Adestrador", Adestrador.class).setMaxResults(2).getResultList();
        for (Adestrador a : ads) {
            a.setNome(a.getNome() + "_Mod");
            session.update(a);
        }
        
        List<Pokedex> pks = session.createQuery("FROM Pokedex", Pokedex.class).setMaxResults(2).getResultList();
        if (pks.size() == 2) {
            pks.get(0).setNome("Bulbasaur_Mod");
            session.update(pks.get(0));
            session.createQuery("UPDATE Pokedex SET nome = 'Ivysaur_Mod' WHERE id = " + pks.get(1).getId()).executeUpdate();
        }
        tx.commit();

        //Listar 
        System.out.println("\n--- Listados previos a XML ---");
        List<Pokedex> pokemonsXML = session.createQuery("FROM Pokedex", Pokedex.class).getResultList();
        List<Adestrador> adestradoresXML = session.createQuery("FROM Adestrador", Adestrador.class).getResultList();
        pokemonsXML.forEach(System.out::println);
        adestradoresXML.forEach(System.out::println);

        //toXML 
        toXML(pokemonsXML, adestradoresXML);

        //Borrar
        tx = session.beginTransaction();
        session.createQuery("DELETE FROM Adestrador").executeUpdate();
        session.createQuery("DELETE FROM Pokedex").executeUpdate();
        tx.commit();
        System.out.println("Datos eliminados de Pokedex e Adestrador.");

        session.close();
        factory.close();
    }

    private static void listarPokedex(Session session, String msg) {
        System.out.println("\n--- " + msg + " ---");
        session.createQuery("FROM Pokedex", Pokedex.class).getResultList().forEach(System.out::println);
    }

    private static void listarAdestradores(Session session, String msg) {
        System.out.println("\n--- " + msg + " ---");
        session.createQuery("FROM Adestrador", Adestrador.class).getResultList().forEach(System.out::println);
    }

    private static void toXML(List<Pokedex> pokemons, List<Adestrador> adestradores) {
        try (FileWriter fwPok = new FileWriter("pokemons.xml"); 
             FileWriter fwAd = new FileWriter("adestradores.xml")) {
            
            fwPok.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<pokemons>\n");
            for (Pokedex p : pokemons) {
                fwPok.write("  <pokemon id=\"" + p.getId() + "\">\n    <nome>" + p.getNome() + "</nome>\n    <peso>" + p.getPeso() + "</peso>\n  </pokemon>\n");
            }
            fwPok.write("</pokemons>");

            fwAd.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<adestradores>\n");
            for (Adestrador a : adestradores) {
                fwAd.write("  <adestrador id=\"" + a.getId() + "\">\n    <nome>" + a.getNome() + "</nome>\n    <nacemento>" + a.getNacemento() + "</nacemento>\n  </adestrador>\n");
            }
            fwAd.write("</adestradores>");

            System.out.println("\n(toXML) Arquivos pokemons.xml e adestradores.xml xerados con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

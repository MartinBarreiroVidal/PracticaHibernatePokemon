import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class MainParte1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = factory.openSession();

        System.out.println("Secuencia executada con obxectos e métodos hibernate:");

        // 1. Inserir 10 pokemons na tabla pokedex
        Transaction tx = session.beginTransaction();
        for (int i = 1; i <= 10; i++) {
            session.save(new Pokedex("Pokemon_" + i, 15.5 + i, "Misc_" + i));
        }
        tx.commit();

        // 2. Listar pokemons na pokedex
        listar(session, "Listado tras insertar 10 pokemons:");

        // 3. Modificar duas entradas na pokedex (una con objeto, otra con HQL)
        tx = session.beginTransaction();
        Pokedex p1 = session.get(Pokedex.class, 1);
        if (p1 != null) {
            p1.setNome("Charmander_Mod");
            session.update(p1);
        }
        session.createQuery("UPDATE Pokedex SET nome = 'Charmeleon_Mod' WHERE id = 12").executeUpdate();
        tx.commit();

        // 4. Listar pokemons na pokedex
        listar(session, "Listado tras modificaciones:");

        // 5. Eliminar tódo-los pokemons na pokedex
        tx = session.beginTransaction();
        session.createQuery("DELETE FROM Pokedex").executeUpdate();
        tx.commit();
        System.out.println("Pokemons eliminados da base de datos.");

        session.close();
        factory.close();
    }

    private static void listar(Session session, String mensaje) {
        System.out.println("\n--- " + mensaje + " ---");
        List<Pokedex> pokemons = session.createQuery("FROM Pokedex", Pokedex.class).getResultList();
        for (Pokedex p : pokemons) {
            System.out.println(p);
        }
        System.out.println("----------------------------------------\n");
    }
}
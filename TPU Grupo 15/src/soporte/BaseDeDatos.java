package soporte;

import java.io.File;
import java.sql.*;

public final class BaseDeDatos {
    public static String dbFilePath = "tpu.db";

    private BaseDeDatos() {
    }

    public static void main(String[] args) {
        if (existeArchivoDB()) {
            eliminarTodasLasTablas();
        }
        crearBaseDeDatos();
    }

    public static boolean existeArchivoDB() {
        File tmpDir = new File(dbFilePath);
        return tmpDir.exists();
    }

    public static void eliminarTodasLasTablas() {
        // Sería más fácil simplemente eliminar el archivo "tpu.sqlite"
        // dado que queremos remover todas las tablas de la DB.
        // Por las dudas solo limpiamos las tablas que vamos a utilizar,
        // así de haber más contenido, éste no se perderá
        try {
            String[] tablas = {
                    "Distritos",
                    "Secciones",
                    "Circuitos",
                    "Mesas",
                    "Agrupaciones",
                    "VotosPresidente",
            };
            for (String tabla : tablas) {
                ejecutarInstruccionSql("DROP TABLE IF EXISTS " + tabla);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void crearBaseDeDatos() {
        String[] tablas = {
                "CREATE TABLE IF NOT EXISTS Distritos (" +
                        " distrito_id INTEGER PRIMARY KEY," +
                        " codigo TEXT NOT NULL UNIQUE," +
                        " nombre TEXT NOT NULL" +
                        ");",
                "CREATE TABLE Secciones (" +
                        " seccion_id INTEGER PRIMARY KEY," +
                        " codigo TEXT NOT NULL UNIQUE," +
                        " nombre TEXT NOT NULL," +
                        " distrito INTEGER NOT NULL," +
                        " FOREIGN KEY(distrito) REFERENCES Distritos(distrito_id)" +
                        ");",
                "CREATE TABLE Circuitos (" +
                        " circuito_id INTEGER PRIMARY KEY," +
                        " codigo TEXT NOT NULL UNIQUE," +
                        " nombre TEXT NOT NULL," +
                        " seccion INTEGER NOT NULL," +
                        " FOREIGN KEY(seccion) REFERENCES Secciones(seccion_id)" +
                        ");",
                "CREATE TABLE Mesas (" +
                        " mesa_id INTEGER PRIMARY KEY," +
                        " codigo TEXT NOT NULL UNIQUE," +
                        " circuito INTEGER NOT NULL," +
                        " FOREIGN KEY(circuito) REFERENCES Circuitos(circuito_id)" +
                        ");",
                "CREATE TABLE Agrupaciones (" +
                        " agrupacion_id INTEGER PRIMARY KEY," +
                        " codigo TEXT NOT NULL UNIQUE," +
                        " nombre TEXT NOT NULL" +
                        ");",
                "CREATE TABLE VotosPresidente (" +
                        " mesa INTEGER NOT NULL UNIQUE," +
                        " agrupacion INTEGER NOT NULL UNIQUE," +
                        " votos INTEGER NOT NULL," +
                        " PRIMARY KEY (mesa, agrupacion)," +
                        " FOREIGN KEY(mesa) REFERENCES Mesas(mesa_id)," +
                        " FOREIGN KEY(agrupacion) REFERENCES Agrupaciones(agrupacion_id)" +
                        ");",
        };
        for (String instruccion : tablas) {
            ejecutarInstruccionSql(instruccion);
        }
    }

    public static void ejecutarInstruccionSql(String comandoSql) {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            System.out.println("Conexíon a la DB abierta.");

            stmt = c.createStatement();
            stmt.executeUpdate(comandoSql);
            stmt.close();
            c.close();
            System.out.println("Conexíon a la DB cerrada.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}

package soporte;

import negocio.Agrupacion;
import negocio.Region;
import negocio.Resultados;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public final class BaseDeDatos {
    public static String dbFilePath = "tpu.db";

    private BaseDeDatos() {
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
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
        // así, de haber más contenido, éste no se perderá
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
                ejecutarInstruccionUpdate("DROP TABLE IF EXISTS " + tabla);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void crearBaseDeDatos() throws SQLException, ClassNotFoundException
    {
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
            ejecutarInstruccionUpdate(instruccion);
        }
    }

    public static Connection obtenerConexionDb() throws SQLException, ClassNotFoundException
    {
        Connection c = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        c.setAutoCommit(false);

        return c;
    }

    public static void ejecutarInstruccionUpdate(String comandoSql) throws SQLException, ClassNotFoundException
    {
        Statement stmt;
        Connection c = obtenerConexionDb();
        System.out.println("Conexión a la DB abierta.");
        stmt = c.createStatement();
        stmt.executeUpdate(comandoSql);
        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");
    }

    public static ResultSet ejecutarInstruccionQuery(String comandoSql) throws SQLException, ClassNotFoundException
    {
        Connection c;
        Statement stmt;
        ResultSet rs = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        c.setAutoCommit(false);
        System.out.println("Conexión a la DB abierta.");

        stmt = c.createStatement();
        rs = stmt.executeQuery(comandoSql);
        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");

        return rs;
    }

    public static ArrayList<Agrupacion> obtenerArgupaciones() throws SQLException, ClassNotFoundException
    {
        ArrayList<Agrupacion> listaAgrupaciones = new ArrayList<Agrupacion>();

        Connection c = obtenerConexionDb();
        Statement stmt;
        System.out.println("Conexión a la DB abierta.");
        stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Agrupaciones;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String  nombre = rs.getString("nombre");
            String  codigo = rs.getString("codigo");

            Agrupacion agrupacion = new Agrupacion(codigo, nombre);
            listaAgrupaciones.add(agrupacion);
        }

        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");

        return listaAgrupaciones;
    }

    public static TSB_OAHashtable identificarAgrupacion() throws SQLException, ClassNotFoundException
    {
        TSB_OAHashtable tablaHash = new TSB_OAHashtable(10);
        for (Agrupacion agrupacion: obtenerArgupaciones() ) {
            tablaHash.put(agrupacion.getCodigo(), agrupacion);
        }
        return tablaHash;
    }

    public static Region identificarRegiones() throws SQLException, ClassNotFoundException
    {
        Region pais = new Region("00","Argentina");
        Region distrito, seccion;
        ResultSet rs;

        Connection c = obtenerConexionDb();
        Statement stmt;
        System.out.println("Conexión a la DB abierta.");
        stmt = c.createStatement();

        // Distritos
        rs = stmt.executeQuery( "SELECT * FROM Distritos;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String codigo = rs.getString("codigo");
            distrito = pais.getOrPutSubregion(codigo);
            distrito.setNombre(nombre);
        }

        // Secciones
        rs = stmt.executeQuery(
                "SELECT S.nombre AS 'NombreSeccion', S.codigo AS 'CodigoSeccion', D.codigo AS 'CodigoDistrito' " +
                        "FROM Secciones S JOIN Distritos D ON D.id=S.distrito;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombreSeccion = rs.getString("NombreSeccion");
            String codigoSeccion = rs.getString("CodigoSeccion");
            String codigoDistrito = rs.getString("CodigoDistrito");
            distrito = pais.getOrPutSubregion(codigoDistrito);
            seccion = distrito.getOrPutSubregion(codigoSeccion);
            seccion.setNombre(nombreSeccion);
        }

        // Circuitos
        rs = stmt.executeQuery( "SELECT C.nombre AS 'NombreCircuito', " +
                "C.codigo AS 'CodigoCircuito', " +
                "S.codigo AS 'CodigoSeccion', " +
                "D.codigo AS 'CodigoDistrito' " +
                "FROM Circuitos C " +
                "JOIN Secciones S ON S.id=C.seccion " +
                "JOIN Distritos D ON D.id=S.distrito;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombreCircuito = rs.getString("NombreCircuito");
            String codigoCircuito = rs.getString("CodigoCircuito");
            String codigoSeccion = rs.getString("CodigoSeccion");
            String codigoDistrito = rs.getString("CodigoDistrito");
            distrito = pais.getOrPutSubregion(codigoDistrito);
            seccion = distrito.getOrPutSubregion(codigoSeccion);
            seccion.agregarSubregion(new Region(codigoCircuito, nombreCircuito));
        }

        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");

        return pais;
    }


    public static Region identificarRegionesSinJoin() throws SQLException, ClassNotFoundException
    {
        Region pais = new Region("00","Argentina");
        Region distrito, seccion;
        ResultSet rs;

        Connection c = obtenerConexionDb();
        Statement stmt;
        System.out.println("Conexión a la DB abierta.");
        stmt = c.createStatement();

        // Distritos
        rs = stmt.executeQuery( "SELECT * FROM Distritos;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String codigo = rs.getString("codigo");
            distrito = pais.getOrPutSubregion(codigo);
            distrito.setNombre(nombre);
        }

        // Secciones
        rs = stmt.executeQuery( "SELECT * FROM Secciones;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String codigo = rs.getString("codigo");
            distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
            seccion = distrito.getOrPutSubregion(codigo);
            seccion.setNombre(nombre);
        }

        // Circuitos
        rs = stmt.executeQuery( "SELECT * FROM Circuitos;" );
        while ( rs.next() ) {
            // int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String codigo = rs.getString("codigo");
            distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
            seccion = distrito.getOrPutSubregion((codigo.substring(0, 5)));
            seccion.agregarSubregion(new Region(codigo, nombre));
        }

        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");

        return pais;
    }

    public static void contarVotosPorRegion(Resultados resultados, Region pais) throws SQLException, ClassNotFoundException {
        int votos;
        // String linea = "", campos[];
        Region distrito, seccion, circuito;

        ResultSet rs;

        Connection c = obtenerConexionDb();
        Statement stmt;
        System.out.println("Conexión a la DB abierta.");
        stmt = c.createStatement();

        // Obtenemos los votos
        rs = stmt.executeQuery( "SELECT V.votos AS 'Votos', " +
                "A.codigo AS 'CodigoAgrupacion', " +
                "M.codigo AS 'CodigoMesa', " +
                "C.codigo AS 'CodigoCircuito', " +
                "S.codigo AS 'CodigoSeccion', " +
                "D.codigo AS 'CodigoDistrito' " +
                "FROM Votos V " +
                "JOIN Agrupaciones A ON V.id_agrupacion=A.id " +
                "JOIN Mesas M ON V.id_mesa=M.id " +
                "JOIN Circuitos C ON C.id=M.circuito " +
                "JOIN Secciones S ON S.id=C.seccion " +
                "JOIN Distritos D ON D.id=S.distrito;");
        while ( rs.next() ) {
            String codigoAgrupacion = rs.getString("CodigoAgrupacion");
            String codigoMesa = rs.getString("CodigoMesa");
            String codigoCircuito = rs.getString("CodigoCircuito");
            String codigoSeccion = rs.getString("CodigoSeccion");
            String codigoDistrito = rs.getString("CodigoDistrito");
            votos = rs.getInt("Votos");

            resultados.sumarVotos("00", codigoAgrupacion, votos); // Votos de toda la Argentina

            // Sumar votos a distrito
            resultados.sumarVotos(codigoDistrito, codigoAgrupacion, votos);

            // Sumar votos a sección
            resultados.sumarVotos(codigoSeccion, codigoAgrupacion, votos);

            // Sumar votos a circuito
            resultados.sumarVotos(codigoCircuito, codigoAgrupacion, votos);

            // Sumar votos a mesa
            resultados.sumarVotos(codigoMesa, codigoAgrupacion, votos);

            // Para mostrar las mesas
            distrito = pais.getSubregion(codigoDistrito);
            seccion = distrito.getSubregion(codigoSeccion);
            circuito = seccion.getSubregion(codigoCircuito);
            circuito.agregarSubregion(new Region(codigoMesa, codigoMesa));
        }
        stmt.close();
        c.close();
        System.out.println("Conexión a la DB cerrada.");
    }
}

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.OutputKeys;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Creamos los objetos
        Cliente c1 = new Cliente(1, "Cliente1", "cliente1@mail.com");
        Cliente c2 = new Cliente(2, "Cliente2", "cliente2@mail.com");
        Cliente c3 = new Cliente(3, "Cliente3", "cliente3@mail.com");

        Pedido p1 = new Pedido(1, 1, "Producto1", 999);
        Pedido p2 = new Pedido(2, 1, "Producto2", 45);
        Pedido p3 = new Pedido(3, 2, "Producto3", 21);
        Pedido p4 = new Pedido(4, 2, "Producto4", 32);
        Pedido p5 = new Pedido(5, 3, "Producto5", 1);

        List<Object> listaObjetos = new ArrayList<>(List.of(c1, c2, c3, p1, p2, p3, p4, p5)); // Lista en la que se agregaran los objetos para poder exportarlos a "objetos.dat"

        // Se crea la ruta donde se guardaran los archivos
        try {
            File carpeta = new File("resources");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("\nError al crear la ruta: " + e.getMessage());
        }

        // Indicamos la ruta con el nombre del fichero
        final String carpeta = "resources";

        // Tratamos de escribir en el fichero con sus correspondientes excepciones
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(carpeta+"/objetos.dat"))) {
            oos.writeObject(listaObjetos);
            System.out.println("\nObjetos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("\nError al guardar objetos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nOcurrio un error: " + e.getMessage());
        }

        // Tratamos de recuperar los objetos del fichero con sus correspondientes excepciones
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(carpeta+"/objetos.dat"))) {
            List<Object> listaObjetosRecuperados = (List<Object>) ois.readObject();
            System.out.println("Objetos recuperados correctamente.");
            for(Object obj : listaObjetosRecuperados) {
                System.out.println(obj);
            }
        } catch (IOException e) {
            System.err.println("\nError al recuperar objetos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nOcurrio un error: " + e.getMessage());

        }

        // Generamos los archivos XML mediante la funcion correspondiente
        try {
            generarClientesXML(c1, c2, c3);
            generarPedidosXML(p1, p2 ,p3 ,p4 ,p5);
            System.out.println("\nArchivos XML guardados correctamente en la carpeta resources.");
        } catch (Exception e) {
            System.err.println("\nError al guardar objetos: " + e.getMessage());
        }

        // Generemos los archivos XSL apartir de los XML y los transformamos en HTML
        try {
            transformarXSL(
                    carpeta + "/clientes.xml",
                    carpeta + "/clientes.xsl",
                    carpeta + "/clientes.html"
            );

            transformarXSL(
                    carpeta + "/pedidos.xml",
                    carpeta + "/pedidos.xsl",
                    carpeta + "/pedidos.html"
            );
        } catch (Exception e) {
            System.err.println("\nError al transformar objetos: " + e.getMessage());
        }

        try {
            consultarXPath("resources/pedidos.xml");
        } catch (Exception e) {
            System.err.println("\nError al consultar objetos: " + e.getMessage());
        }
    }

    // Funcion para generar XML
    public static void generarClientesXML(Cliente... clientes) throws Exception {
        DocumentBuilderFactory fabrica =  DocumentBuilderFactory.newInstance(); // Instanciador de "la fabrica"
        DocumentBuilder constructor = fabrica.newDocumentBuilder(); // Creamos un constructor para el documento mediante "la fabrica"
        Document documento = constructor.newDocument(); // Creamos el documento mediante el constructor
        Element raiz = documento.createElement("clientes"); // Creamos el elemento "raiz" del archivo XML donde iran nuestros "TAGs"
        documento.appendChild(raiz); // Añadimos al documento XML nuestra "raiz"

        for (Cliente c : clientes) {
            // Bucle con el que en creamos una TAG "raiz" dentro con otros TAGs con los datos de cada cliente
            Element cliente = documento.createElement("cliente");
            raiz.appendChild(cliente);

            Element id = documento.createElement("id");
            id.setTextContent(String.valueOf(c.getId()));
            cliente.appendChild(id);

            Element nombre = documento.createElement("nombre");
            nombre.setTextContent(c.getNombre());
            cliente.appendChild(nombre);

            Element email = documento.createElement("email");
            email.setTextContent(c.getEmail());
            cliente.appendChild(email);
        }

        guardarXML(documento, "resources/clientes.xml"); // Llamamos a la funcion para guardar el archivo XML
    }

    public static void generarPedidosXML(Pedido... pedidos) throws Exception {
        DocumentBuilderFactory fabrica =  DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = fabrica.newDocumentBuilder();
        Document documento = constructor.newDocument();
        Element raiz = documento.createElement("pedidos");
        documento.appendChild(raiz);

        for (Pedido p : pedidos) {
            Element pedido = documento.createElement("pedido");
            raiz.appendChild(pedido);

            Element id = documento.createElement("id");
            id.setTextContent(String.valueOf(p.getId()));
            pedido.appendChild(id);

            Element idcliente = documento.createElement("idcliente");
            idcliente.setTextContent(String.valueOf(p.getClienteId()));
            pedido.appendChild(idcliente);

            Element producto = documento.createElement("producto");
            producto.setTextContent(String.valueOf(p.getProducto()));
            pedido.appendChild(producto);

            Element cantidad = documento.createElement("cantidad");
            cantidad.setTextContent(String.valueOf(p.getCantidad()));
            pedido.appendChild(cantidad);
        }

        guardarXML(documento, "resources/pedidos.xml");
    }

    public static void guardarXML(Document documento, String ruta) throws Exception {
        TransformerFactory fabrica = TransformerFactory.newInstance(); // Instanciamos la fabrica
        Transformer transformador = fabrica.newTransformer(); // Instanciamos un transformador mediante la fabrica
        transformador.setOutputProperty(OutputKeys.INDENT, "yes"); // Indicamos que el XML se escriba con sangria mediante el transformador
        transformador.transform(new DOMSource(documento), new StreamResult(new File(ruta))); // Guardamos el archivo XML mediante el transformador
    }

    public static void transformarXSL(String xml, String XSL, String HTML) throws Exception {
        // Funcion con la que transformaremos el XML usando una plantilla XSL a HTML
        TransformerFactory fabrica = TransformerFactory.newInstance(); // Instanciamos la fabrica
        Transformer transformador = fabrica.newTransformer(new StreamSource(new File(XSL))); // Instanciamos el transformador mediante la fabrica otorgandole el XSL

        transformador.transform(new StreamSource(new File(xml)), new StreamResult((new File(HTML)))); // Transformamos el XML usando el XSL a HTML

        System.out.println("\nHTML Generado: " + new File(HTML).getAbsolutePath());
    }

    public static void consultarXPath(String rutaXML) throws Exception {

        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance(); // Instanciamos la fabrica
        DocumentBuilder constructor = fabrica.newDocumentBuilder();  // Instanciamos el constructor mediante la fabrica
        Document documento = constructor.parse(new File(rutaXML));  // Instanciamos el documento mediante el constructor que obtiene el XML
                                                                   // (Tiene que obtener el XML del cual generaremos la consulta al tener 2 XML nos tenemos que asegurar de que obtiene por parametro con el que trabajaremos)

        XPath xPath = XPathFactory.newInstance().newXPath(); // Instanciamos XPath para las consultas

        // Consulta XPath para verificar por cantidad
        String cantidad = "100";
        String consulta1 = "/pedidos/pedido[cantidad> " + cantidad + "]"; // Indicamos la consulta con sus parametros

        NodeList pedidosCantidad = (NodeList) xPath.evaluate(
                // Creamos un NodeList en el cual evaluara la consulta con el documento
                consulta1,
                documento,
                XPathConstants.NODESET
        );

        // Obtenemos los resultados
        System.out.println("\nPedidos con cantidad mayor que " + cantidad + " :");

        for(int i = 0; i < pedidosCantidad.getLength(); i++) {
            System.out.println(pedidosCantidad.item(i).getTextContent());
        }

        // Consulta XPath para verificar por clienteid
        String idVerificar = "1";
        String consulta2 = "/pedidos/pedido[idcliente = " +  idVerificar + " ]";

        NodeList pedidosCliente = (NodeList) xPath.evaluate(
                consulta2,
                documento,
                XPathConstants.NODESET
        );

        System.out.println("\nPedidos del cliente con ID " + idVerificar + " :");

        for (int i = 0; i < pedidosCliente.getLength(); i++) {
            System.out.println(pedidosCliente.item(i).getTextContent());
        }

        // Consulta XPath para verificar por correo
        Document documentoClientes = constructor.parse(new File("resources/clientes.xml"));
        String nombre = "Cliente1";
        String consulta3 = "/clientes/cliente[nombre = '" + nombre + "']";

        NodeList Clientes = (NodeList) xPath.evaluate(
                consulta3,
                documentoClientes,
                XPathConstants.NODESET
        );

        System.out.println("\nDetalles del cliente con ID " + nombre + " :");

        for (int i = 0; i < Clientes.getLength(); i++) {
            System.out.println(Clientes.item(i).getTextContent());
        }
    }
}

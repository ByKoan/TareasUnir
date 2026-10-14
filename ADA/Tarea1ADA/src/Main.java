import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


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

        try {
            File carpeta = new File("resources");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Error al crear la ruta: " + e.getMessage());
        }

        // Indicamos la ruta con el nombre del fichero
        final String carpeta = "resources";

        // Tratamos de escribir en el fichero con sus correspondientes excepciones
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(carpeta+"/objetos.dat"))) {
            oos.writeObject(listaObjetos);
            System.out.println("Objetos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar objetos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }

        // Tratamos de recuperar los objetos del fichero con sus correspondientes excepciones
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(carpeta+"/objetos.dat"))) {
            List<Object> listaObjetosRecuperados = (List<Object>) ois.readObject();
            System.out.println("Objetos recuperados correctamente.");
            for(Object obj : listaObjetosRecuperados) {
                System.out.println(obj);
            }
        } catch (IOException e) {
            System.err.println("Error al recuperar objetos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());

        }

        try {
            generarClientesXML(c1, c2, c3);
            generarPedidosXML(p1, p2 ,p3 ,p4 ,p5);
            System.out.println("Archivos XML guardados correctamente en la carpeta resources.");
        } catch (Exception e) {
            System.err.println("Error al guardar objetos: " + e.getMessage());
        }

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
            System.err.println("Error al transformar objetos: " + e.getMessage());
        }
    }

    public static void generarClientesXML(Cliente... clientes) throws Exception {
        DocumentBuilderFactory fabrica =  DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = fabrica.newDocumentBuilder();
        Document documento = constructor.newDocument();
        Element raiz = documento.createElement("clientes");
        documento.appendChild(raiz);

        for (Cliente c : clientes) {
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

        guardarXML(documento, "resources/clientes.xml");
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
        TransformerFactory fabrica = TransformerFactory.newInstance();
        Transformer transformador = fabrica.newTransformer();
        transformador.setOutputProperty(OutputKeys.INDENT, "yes");
        transformador.transform(new DOMSource(documento), new StreamResult(new File(ruta)));
    }

    public static void transformarXSL(String xml, String XSL, String HTML) throws Exception {
        TransformerFactory fabrica = TransformerFactory.newInstance();
        Transformer transformador = fabrica.newTransformer(new StreamSource(new File(XSL)));

        transformador.transform(new StreamSource(new File(xml)), new StreamResult((new File(HTML))));

        System.out.println("HTML Generado: " + new File(HTML).getAbsolutePath());
    }
}

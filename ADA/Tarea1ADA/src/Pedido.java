import java.io.Serializable;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private int clienteId;
    private String producto;
    private int cantidad;

    public Pedido() {

    }

    public Pedido(int id, int clienteId, String producto, int cantidad) {
        this.id = id;
        this.clienteId = clienteId;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Pedido [id= " + id + ", cliente id= " + clienteId + ", producto= " + producto + ", cantidad= " + cantidad + ']';
    }
}

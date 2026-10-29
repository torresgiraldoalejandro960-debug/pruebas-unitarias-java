package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * ENTREGA DEL TALLER
 * Nombre completo: Alejandro Torres Giraldo
 * 1. Bug encontrado (método y línea de CarritoCompras.java):
 *    Método agregarProducto, línea 24. El if de la cantidad usaba "cantidad < 0",
 *    lo que dejaba pasar la cantidad 0. La regla R1 exige cantidad mayor a 0,
 *    por lo que debía ser "cantidad <= 0".
 * 2. Prueba que lo detectó (nombre del método):
 *    agregarProducto_cantidadCero_lanzaExcepcion
 * 3. Sugerencia de la IA que rechacé o corregí, y por qué:
 *    Rechacé una prueba de cantidadDeItems() que usaba assertNotNull. Ese método
 *    devuelve un int, y un int nunca puede ser null, así que la aserción pasaría
 *    siempre, incluso si el método devolviera 99. No verifica nada. La reemplacé por
 *    assertEquals(2, ...) con dos productos agregados y assertEquals(0, ...) con
 *    el carrito vacío, cuyos valores salen de contar los productos agregados.
 */
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        // Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        // Act
        double subtotal = carrito.calcularSubtotal();
        // Assert
        assertEquals(34_000, subtotal, 0.001);
    }

    // TODO Parte A: carrito vacío + una prueba por cada entrada inválida de R1

    @Test
    @DisplayName("Carrito vacío: subtotal 0 y total 0")
    void calcularTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal(), 0.001);
        assertEquals(0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Rechaza un nombre nulo")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(null, 5_000, 1));
    }

    @Test
    @DisplayName("Rechaza un nombre vacío")
    void agregarProducto_nombreVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("", 5_000, 1));
    }

    @Test
    @DisplayName("Rechaza un precio igual a 0")
    void agregarProducto_precioCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 0, 1));
    }

    @Test
    @DisplayName("Rechaza un precio negativo")
    void agregarProducto_precioNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", -100, 1));
    }
    @Test
    @DisplayName("Rechaza una cantidad igual a 0")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 5_000, 0));
    }
    @Test
    @DisplayName("Rechaza una cantidad negativa")
    void agregarProducto_cantidadNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Lápiz", 5_000, -1));
    }
    // TODO Parte B: SENA10, BIENVENIDO (50.000 y 49.999), cupón inválido, totales (R6)

    @Test
    @DisplayName("SENA10 descuenta el 10 % del subtotal")
    void calcularDescuento_cuponSena10_retornaDiezPorCiento() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(10_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal de $50.000 descuenta $5.000")
    void calcularDescuento_bienvenidoEnLimite_descuentaCincoMil() {
        carrito.agregarProducto("Resma", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(5_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal de $49.999 no descuenta")
    void calcularDescuento_bienvenidoBajoElLimite_noDescuenta() {
        carrito.agregarProducto("Resma", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Un cupón desconocido se rechaza")
    void aplicarCupon_codigoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.aplicarCupon("GRATIS"));

    }
    @Test
    @DisplayName("Total con cupón SENA10 y envío pagado")
    void calcularTotal_conSena10EnvioPagado_retornaTotalCorrecto() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(119_100, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total sin cupón y con envío pagado")
    void calcularTotal_sinCuponEnvioPagado_retornaTotalCorrecto() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con cupón SENA10 y envío gratis")
    void calcularTotal_conSena10EnvioGratis_retornaTotalCorrecto() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }
    @Test
    @DisplayName("Cuenta los productos agregados")
    void cantidadDeItems_dosProductosDistintos_retornaDos() {
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        assertEquals(2, carrito.cantidadDeItems());
    }

    @Test
    @DisplayName("Un carrito vacío tiene 0 productos")
    void cantidadDeItems_carritoVacio_retornaCero() {
        assertEquals(0, carrito.cantidadDeItems());
    }
}
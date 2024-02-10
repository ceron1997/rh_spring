package gm.rh.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gm.rh.excepcion.RecursonoEncontrado;
import gm.rh.modelo.Empleado;
import gm.rh.servicio.IEmpleadoServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("rh")
@CrossOrigin(value = "http://localhost:3000")
public class EmpleadoControlador {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);
    @Autowired
    private IEmpleadoServicio empleadoServicio;

    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleado() {
        var empleados = empleadoServicio.listarEmpleado();
        empleados.forEach(empleado -> logger.info(empleado.toString()));
        return empleados;
    }

    @PostMapping("/empleados")
    public Empleado guardarEmpleado(@RequestBody Empleado empleado) {

        logger.info("guardando objeto: " + empleado);
        return empleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("/empleado/{id}")
    public ResponseEntity<Empleado> getEmpleadobyID(@PathVariable Integer id) {
        Empleado empleado = empleadoServicio.buscarEmpleadoporId(id);
        if (empleado == null){
            throw new RecursonoEncontrado("No existe el registro con id" + id); 
        }
        return ResponseEntity.ok(empleado); 
    }

    @PutMapping("/empleado/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Integer id, @RequestBody Empleado emp) {
        Empleado empleado = empleadoServicio.buscarEmpleadoporId(id);
        if (empleado == null){
            throw new RecursonoEncontrado("No existe el registro con id" + id); 
        }
        empleado.setNombre(emp.getNombre());
        empleado.setDepartamento(emp.getDepartamento());
        empleado.setSueldo(emp.getSueldo());
        empleadoServicio.guardarEmpleado(empleado); 
        return ResponseEntity.ok(empleado);
    }
    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Integer id ){
        Empleado empleado = empleadoServicio.buscarEmpleadoporId(id);
        if (empleado == null){
            throw new RecursonoEncontrado("No existe el registro con id" + id); 
        }
        empleadoServicio.eliminarEmpleado(empleado);
        // la respuesta es un json {"eliminado": true}
        Map<String, Boolean> respuesta = new HashMap<>(); 
        respuesta.put("eliminado", true); 
        return ResponseEntity.ok(respuesta); 
    }

}

package com.ipartek.formacion.gv.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.formacion.modelo.daos.CocheDAO;
import com.ipartek.formacion.modelo.pojo.Coche;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@Api(tags = { "VEHICULO" }, produces = "application/json", description="Gestión de vehiculos")
public class VehiculoController {
	
	private final static Logger LOG = Logger.getLogger(VehiculoController.class);
	private static CocheDAO cocheDAO;
	private ValidatorFactory factory;
	private Validator validator;

	public  VehiculoController() {
		super();
		cocheDAO = CocheDAO.getInstance();
		factory  = Validation.buildDefaultValidatorFactory();
    	validator  = factory.getValidator();
	}
	
	
	
	@RequestMapping( value= {"/api/vehiculo"},method = RequestMethod.GET)
	public ArrayList<Coche> listar(){
		ArrayList<Coche> coches = new ArrayList<Coche>();
		coches=cocheDAO.getAll();
		return coches;
	}
	
	
	@RequestMapping( value= {"/api/vehiculo/{id}"},method = RequestMethod.GET)
	@ApiResponses({
		@ApiResponse(code = 200 , message = "Detalle"),
		@ApiResponse(code = 404 , message = "No se encuentra"),
		@ApiResponse(code = 400 , message = "Datos Vehiculos No Validos"),
		@ApiResponse(code = 500 , message = "Error servidor")
		})
	public ResponseEntity<Coche> detalle(@PathVariable String id){
		Coche c = new Coche();
		ResponseEntity<Coche> response = new ResponseEntity<Coche>(HttpStatus.INTERNAL_SERVER_ERROR);

		try {
			Long identificador=Long.parseLong(id);
			c = cocheDAO.getById(identificador);
			if (c!=null) {
				 response = new ResponseEntity<Coche>(c,HttpStatus.OK);
			}else {
				response = new ResponseEntity<Coche>(c,HttpStatus.NOT_FOUND);
			}
		}catch (NumberFormatException e) {
			response = new ResponseEntity<Coche>(HttpStatus.BAD_REQUEST);
			LOG.debug(HttpStatus.BAD_REQUEST+" "+ e);
		}catch(Exception e) {
			LOG.error(e);
		}
		
		return response;
	}
	
	
	
	
	@RequestMapping( value= {"/api/vehiculo/{id}"},method = RequestMethod.DELETE)
	@ApiResponses({
		@ApiResponse(code = 200 , message = "Eliminado"),
		@ApiResponse(code = 404 , message = "No se encuentra"),
		@ApiResponse(code = 400 , message = "Datos Vehiculos No Validos"),
		@ApiResponse(code = 500 , message = "Error servidor")
		})
	public ResponseEntity<Coche> eliminar(@PathVariable String id){
		
		ResponseEntity<Coche> response = new ResponseEntity<Coche>(HttpStatus.NOT_FOUND);
		Long idp=0L;
		try {
			idp=Long.parseLong(id);//se intenta parsear si no puede es k no es numero 
	
					try {
						boolean b = cocheDAO.delete(idp);
						if (b==true) {//coche eliminado 200 
							 response = new ResponseEntity<Coche>(HttpStatus.OK);
						}else if(b==false) {
							response = new ResponseEntity<Coche>(HttpStatus.NOT_FOUND);
						}
					}catch(SQLException e) {//coche con conflicto 409
						response = new ResponseEntity<Coche>(HttpStatus.CONFLICT);
						LOG.debug("no se puede eliminar coche con multas asociadas");
					}
		}catch(NumberFormatException e) {//Captura la excepcion del parseo y envia status error: 400
			response = new ResponseEntity<Coche>(HttpStatus.BAD_REQUEST);
			LOG.debug(HttpStatus.BAD_REQUEST+" "+ e);
		}catch(Exception e) {
			response = new ResponseEntity<Coche>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	
	
	
	@RequestMapping( value= {"/api/vehiculo/"}, method = RequestMethod.POST)
	@ApiResponses({
		@ApiResponse(code = 201 , message = "Vehiculo Creado"),
		@ApiResponse(code = 409 , message = "Existe Matricula"),
		@ApiResponse(code = 400 , message = "Datos Vehiculos No Validos"),
		@ApiResponse(code = 500 , message = "Error servidor")
		})
	public ResponseEntity<Coche> crear( @RequestBody Coche coche ){
		
		ResponseEntity<Coche> response = new ResponseEntity<Coche>( HttpStatus.INTERNAL_SERVER_ERROR );
		try {
			
			Set<ConstraintViolation<Coche>> violations = validator.validate(coche);
			if ( violations.size() > 0) {		
				LOG.debug("Coche no valido " + violations);
				response = new ResponseEntity<Coche>( HttpStatus.BAD_REQUEST );				
				
			}else {				
				Coche c  = cocheDAO.insert(coche);
				response = new ResponseEntity<Coche>( c, HttpStatus.CREATED );		
				LOG.info("Nuevo Coche creado " + c );
			}
		}catch (SQLException e) {	
			LOG.debug("Ya existe matricula " + coche.getMatricula());
			response = new ResponseEntity<Coche>( HttpStatus.CONFLICT );
			
		}catch (Exception e) {
			LOG.error(e);
			response = new ResponseEntity<Coche>( HttpStatus.INTERNAL_SERVER_ERROR );
		}		
		return response;		
	}
	
	
	
	
	
	@RequestMapping( value= {"/api/vehiculo/{id}"},method = RequestMethod.PATCH)
	@ApiResponses({
		@ApiResponse(code = 200 , message = "Detalle"),
		@ApiResponse(code = 404 , message = "No se encuentra"),
		@ApiResponse(code = 400 , message = "Datos Vehiculos No Validos"),
		@ApiResponse(code = 500 , message = "Error servidor")
		})
	public ResponseEntity<Coche> baja(@PathVariable String id){
ResponseEntity<Coche> response = new ResponseEntity<Coche>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		try {
			Long identificador=Long.parseLong(id);
			boolean b = cocheDAO.baja(identificador,true);
			if (b==true) {
				 response = new ResponseEntity<Coche>(HttpStatus.OK);
			}else if(b==false) {
				response = new ResponseEntity<Coche>(HttpStatus.NOT_FOUND);
			}
			
		}catch(SQLException e) {//coche con conflicto 409 multa asociadas
			response = new ResponseEntity<Coche>(HttpStatus.CONFLICT);
			LOG.debug(HttpStatus.CONFLICT+" "+ e);
		}catch (NumberFormatException e) {
			response = new ResponseEntity<Coche>(HttpStatus.BAD_REQUEST);
			LOG.debug(HttpStatus.BAD_REQUEST+" "+ e);
		}catch(Exception e) {
			LOG.error(e);
		}
		
		return response;
	}
	

	
	
	@RequestMapping(value = { "/api/vehiculo/{id}" }, method = RequestMethod.PUT)
	@ApiResponses({
		@ApiResponse(code = 200 , message = "Detalle"),
		@ApiResponse(code = 404 , message = "No se encuentra"),
		@ApiResponse(code = 400 , message = "Datos Vehiculos No Validos"),
		@ApiResponse(code = 500 , message = "Error servidor")
		})
	public ResponseEntity<Coche> modificar(@RequestBody Coche coche, @PathVariable String id) throws SQLException {
ResponseEntity<Coche> response = new ResponseEntity<Coche>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		try {
			Long identificador=Long.parseLong(id);
			coche.setId(identificador);
			boolean b = cocheDAO.update(coche);
			if (b==true) {
				 response = new ResponseEntity<Coche>(HttpStatus.OK);
			}
			
		}catch(SQLException e) {//coche con conflicto 409
			response = new ResponseEntity<Coche>(HttpStatus.CONFLICT);
			LOG.debug(HttpStatus.CONFLICT+" "+ e);
		}catch (NumberFormatException e) {
			response = new ResponseEntity<Coche>(HttpStatus.BAD_REQUEST);
			LOG.debug(HttpStatus.BAD_REQUEST+" "+ e);
		}catch(Exception e) {
			LOG.error(e);
		}
		
		return response;
	
}
}

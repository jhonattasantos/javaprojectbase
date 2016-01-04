package com.javaprojectbase.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.javaprojectbase.DAO.CarrinhoDAO;
import com.javaprojectbase.models.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

	private static Map<String, Carrinho> map = new HashMap<>();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<Carrinho>> index() {
//		List<Carrinho> carrinhos = new CarrinhoDAO().findAll();
		Map<String,List<Carrinho>> newmap = new HashMap<>();
		List<Carrinho> carrinhos = new CarrinhoDAO().findAll();
		newmap.put("carrinho", carrinhos);
		return newmap;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Carrinho save(Carrinho carrinho) {
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		carrinho = carrinhoDAO.save(carrinho);
		return carrinho;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Map<String, Carrinho> show(@PathParam("id") int id) {
		Carrinho carrinho = new CarrinhoDAO().findById(new Long(id));
		map.put("carrinho", carrinho);
		if (carrinho == null) {
			throw new WebApplicationException(Response.status(404).entity("No product with id " + id).build());
		}
		return map;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Carrinho update(@PathParam("id") int id) {
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		Carrinho carrinho = carrinhoDAO.findById(new Long(id));
		carrinhoDAO.save(carrinho);
		return carrinho;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public String remove(@PathParam("id") int id) {
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		Carrinho carrinho = carrinhoDAO.findById(new Long(id));
		if (carrinho == null) {
			return "Não existe esse carrinho";
		}
		carrinhoDAO.remove(carrinho);
		return "carrinho Removido";
	}

}

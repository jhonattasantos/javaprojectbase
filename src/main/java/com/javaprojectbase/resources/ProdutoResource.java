package com.javaprojectbase.resources;

import java.util.List;

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

import com.javaprojectbase.DAO.ProdutoDAO;
import com.javaprojectbase.models.Produto;

@Path("produtos")
public class ProdutoResource {

//	private static Map<String, Produto> p = new HashMap<>();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Produto> index() {
		List<Produto> produtos = new ProdutoDAO().findAll();
		return produtos;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Produto save(Produto produto) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produto = produtoDAO.save(produto);
		return produto;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Produto show(@PathParam("id") int id) {
		Produto produto = new ProdutoDAO().findById(new Long(id));
		if (produto == null) {
			throw new WebApplicationException(Response.status(404).entity("No product with id " + id).build());
		}
		return produto;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Produto update(@PathParam("id") int id) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.findById(new Long(id));
		produto.setNome("Produto Atualizado de novo mais uma vez");
		produtoDAO.save(produto);
		return produto;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public String remove(@PathParam("id") int id) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.findById(new Long(id));
		if (produto == null) {
			return "Não existe esse produto";
		}
		produtoDAO.remove(produto);
		return "Produto Removido";
	}

}

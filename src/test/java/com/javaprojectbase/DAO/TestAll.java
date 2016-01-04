package com.javaprojectbase.DAO;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.javaprojectbase.DAO.CarrinhoDAO;
import com.javaprojectbase.DAO.ProdutoDAO;
import com.javaprojectbase.models.Carrinho;
import com.javaprojectbase.models.Produto;

public class TestAll{
	
	@Test
	public void insertProduct(){
		Produto produto = new Produto();
		ProdutoDAO produtoDAO = new ProdutoDAO();
		int before = produtoDAO.findAll().size()+1;
		produto.setNome("Mouse do futuro");	
		produto.setPreco(new BigDecimal("100.89"));
		produto.setDescricao("Mouse do garai");
		produtoDAO.save(produto);
		int after = produtoDAO.findAll().size();		
		assertEquals(after, before);
	}

	@Test
	public void insertProductAndCarrinho(){
		Produto produto = new Produto();
		ProdutoDAO produtoDAO = new ProdutoDAO();
		int before = produtoDAO.findAll().size()+1;
		produto.setNome("Teclado do futuro");	
		produto.setPreco(new BigDecimal("50.89"));
		produto.setDescricao("Teclado do garai");
		produtoDAO.save(produto);
		int after = produtoDAO.findAll().size();
		
		Carrinho carrinho = new Carrinho();
		carrinho.setDataCriacao(Calendar.getInstance());
		carrinho.adicionarProduto(produto);
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		carrinhoDAO.save(carrinho);
		
		assertEquals(after, before);
	}
	
	@Test
	public void findProductID(){
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = (Produto) produtoDAO.findById(1L);
		System.out.println(produto.getNome());
	}
	
	@Test
	public void findall(){
		ProdutoDAO produtoDAO = new ProdutoDAO();
		List<Produto> produtos = produtoDAO.findAll();
		for (Produto produto : produtos) {
			System.out.println(produto.getId()+" - " +produto.getNome());
		}
	}
	
	@Test
	public void updateProduct(){
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.findById(22L);
		produto.setNome("modificado geral");		
		produtoDAO.save(produto);		
		System.out.println(produto.getNome());
	}
	
	@Test
	public void removeProduct(){
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.findById(22L);
		produtoDAO.remove(produto);		
	}
}

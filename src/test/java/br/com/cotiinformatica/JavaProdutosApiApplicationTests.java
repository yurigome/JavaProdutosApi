package br.com.cotiinformatica;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import br.com.cotiinformatica.dtos.CategoriaGetDto;
import br.com.cotiinformatica.dtos.FornecedorGetDto;
import br.com.cotiinformatica.dtos.ProdutoGetDto;
import br.com.cotiinformatica.dtos.ProdutoPostDto;
import br.com.cotiinformatica.dtos.ProdutoPutDto;

@SpringBootTest
@AutoConfigureMockMvc // executar os testes na API
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // ordem dos testes
class JavaProdutosApiApplicationTests {
	@Autowired
	MockMvc mockMvc; // realizar as chamadas para a API
	@Autowired
	ObjectMapper objectMapper; // envio dos dados para a API

	// objeto para armazenar os dados do produto cadastrado
	private static ProdutoGetDto produto;

	@Test
	@Order(1)
	public void cadastrarProdutoTest() throws Exception {

		// consultar as categorias na API
		MvcResult resultCategorias = mockMvc.perform(get("/api/categorias")).andReturn();
		String contentCategorias = resultCategorias.getResponse().getContentAsString();
		List<CategoriaGetDto> categorias = objectMapper.readValue(contentCategorias,
				new TypeReference<List<CategoriaGetDto>>() {
				});

		// consultar os fornecedores na API
		MvcResult resultFornecedores = mockMvc.perform(get("/api/fornecedores")).andReturn();
		String contentFornecedores = resultFornecedores.getResponse().getContentAsString();
		List<FornecedorGetDto> fornecedores = objectMapper.readValue(contentFornecedores,
				new TypeReference<List<FornecedorGetDto>>() {
				});

		// criando os dados do produto
		ProdutoPostDto dto = new ProdutoPostDto();
		Faker faker = new Faker();

		dto.setNome(faker.commerce().productName());
		dto.setPreco((double) faker.number().randomDigit());
		dto.setQuantidade(faker.number().randomDigit());
		dto.setIdCategoria(categorias.get(0).getIdCategoria());
		dto.setIdFornecedor(fornecedores.get(0).getIdFornecedor());

		// cadastrar o produto
		MvcResult resultProduto = mockMvc.perform(
				post("/api/produtos").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk()).andReturn();

		// lendo os dados obtidos da API referente ao produto que foi cadastrado
		String contentProduto = resultProduto.getResponse().getContentAsString();
		produto = objectMapper.readValue(contentProduto, ProdutoGetDto.class);
	}

	@Test
	@Order(2)
	public void atualizarProdutoTest() throws Exception {

		ProdutoPutDto dto = new ProdutoPutDto();
		Faker faker = new Faker();

		dto.setIdProduto(produto.getIdProduto());
		dto.setNome(faker.commerce().productName());
		dto.setPreco((double) faker.number().randomDigit());
		dto.setQuantidade(faker.number().randomDigit());
		dto.setIdCategoria(produto.getCategoria().getIdCategoria());
		dto.setIdFornecedor(produto.getFornecedor().getIdFornecedor());

		mockMvc.perform(
				put("/api/produtos").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void consultarProdutoTest() throws Exception {

		mockMvc.perform(get("/api/produtos/" + produto.getIdProduto())).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	public void excluirProdutoTest() throws Exception {

		mockMvc.perform(delete("/api/produtos/" + produto.getIdProduto())).andExpect(status().isOk());
	}
}

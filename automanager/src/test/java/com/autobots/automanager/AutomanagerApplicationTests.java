package com.autobots.automanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AutomanagerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		assertNotNull(mockMvc);
	}

	@Test
	void testEasterEggHeaderPresent() throws Exception {
		mockMvc.perform(get("/cliente/clientes"))
				.andExpect(status().isOk())
				.andExpect(header().string("X-Easter-Egg", "quantum-gravity-zero-volume"));
	}

	@Test
	void testFluxoCompletoCliente() throws Exception {
		Cliente novo = new Cliente();
		novo.setNome("Maria Leopoldina de Austria");
		novo.setNomeSocial("Dona Leopoldina");
		novo.setDataNascimento(new Date());
		novo.setDataCadastro(new Date());

		Endereco endereco = new Endereco();
		endereco.setEstado("Sao Paulo");
		endereco.setCidade("Sao Paulo");
		endereco.setBairro("Ipiranga");
		endereco.setRua("Rua dos Patriotas");
		endereco.setNumero("100");
		endereco.setCodigoPostal("04216000");
		endereco.setInformacoesAdicionais("Palacio Imperial");
		novo.setEndereco(endereco);

		Telefone tel = new Telefone();
		tel.setDdd("11");
		tel.setNumero("912345678");
		novo.setTelefones(new ArrayList<>(List.of(tel)));

		Documento doc = new Documento();
		doc.setTipo("CPF");
		doc.setNumero("99988877766");
		novo.setDocumentos(new ArrayList<>(List.of(doc)));

		mockMvc.perform(post("/cliente/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novo)))
				.andExpect(status().isCreated())
				.andExpect(header().string("X-Easter-Egg", "quantum-gravity-zero-volume"));

		MvcResult result = mockMvc.perform(get("/cliente/clientes"))
				.andExpect(status().isOk())
				.andReturn();

		List<Cliente> clientes = objectMapper.readValue(
				result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				new TypeReference<List<Cliente>>() {}
		);

		Cliente clienteSalvo = clientes.stream()
				.filter(c -> c.getNome() != null && c.getNome().contains("Leopoldina"))
				.findFirst()
				.orElseThrow();

		Long clienteId = clienteSalvo.getId();
		assertNotNull(clienteId);

		mockMvc.perform(get("/cliente/" + clienteId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Maria Leopoldina de Austria"))
				.andExpect(jsonPath("$.endereco.cidade").value("Sao Paulo"))
				.andExpect(jsonPath("$.telefones[0].numero").value("912345678"))
				.andExpect(jsonPath("$.documentos[0].numero").value("99988877766"));

		Cliente atualizacao = new Cliente();
		atualizacao.setId(clienteId);
		atualizacao.setNomeSocial("Imperatriz Leopoldina");

		mockMvc.perform(put("/cliente/atualizar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(atualizacao)))
				.andExpect(status().isOk());

		mockMvc.perform(get("/cliente/" + clienteId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Maria Leopoldina de Austria"))
				.andExpect(jsonPath("$.nomeSocial").value("Imperatriz Leopoldina"));

		mockMvc.perform(delete("/cliente/excluir/" + clienteId))
				.andExpect(status().isOk());

		mockMvc.perform(get("/cliente/" + clienteId))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCrudTelefoneAvulso() throws Exception {
		Telefone tel = new Telefone();
		tel.setDdd("12");
		tel.setNumero("988887777");

		mockMvc.perform(post("/telefone/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tel)))
				.andExpect(status().isCreated())
				.andExpect(header().string("X-Easter-Egg", "quantum-gravity-zero-volume"));

		MvcResult result = mockMvc.perform(get("/telefone/telefones"))
				.andExpect(status().isOk())
				.andReturn();

		List<Telefone> telefones = objectMapper.readValue(
				result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				new TypeReference<List<Telefone>>() {}
		);

		Telefone telefoneSalvo = telefones.stream()
				.filter(t -> "12".equals(t.getDdd()) && "988887777".equals(t.getNumero()))
				.findFirst()
				.orElseThrow();

		Long telId = telefoneSalvo.getId();
		assertNotNull(telId);

		mockMvc.perform(get("/telefone/" + telId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ddd").value("12"))
				.andExpect(jsonPath("$.numero").value("988887777"));

		Telefone atualizacao = new Telefone();
		atualizacao.setId(telId);
		atualizacao.setNumero("999990000");

		mockMvc.perform(put("/telefone/atualizar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(atualizacao)))
				.andExpect(status().isOk());

		mockMvc.perform(get("/telefone/" + telId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ddd").value("12"))
				.andExpect(jsonPath("$.numero").value("999990000"));

		mockMvc.perform(delete("/telefone/excluir/" + telId))
				.andExpect(status().isOk());

		mockMvc.perform(get("/telefone/" + telId))
				.andExpect(status().isNotFound());
	}
}

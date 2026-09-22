package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;

	@GetMapping({"/clientes", ""})
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping({"/{id}", "/cliente/{id}"})
	public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
		List<Cliente> clientes = repositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	@PostMapping({"/cadastro", ""})
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping({"/atualizar", ""})
	public ResponseEntity<?> atualizarCliente(@RequestBody Cliente atualizacao) {
		if (atualizacao == null || atualizacao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Cliente> opt = repositorio.findById(atualizacao.getId());
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Cliente cliente = opt.get();
		ClienteAtualizador atualizador = new ClienteAtualizador();
		atualizador.atualizar(cliente, atualizacao);
		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping({"/{id}", "/excluir/{id}"})
	public ResponseEntity<?> excluirClientePorId(@PathVariable Long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(opt.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirCliente(@RequestBody(required = false) Cliente exclusao) {
		if (exclusao == null || exclusao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Cliente> opt = repositorio.findById(exclusao.getId());
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(opt.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@GetMapping({"/enderecos", ""})
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		return new ResponseEntity<>(enderecos, HttpStatus.OK);
	}

	@GetMapping({"/{id}", "/endereco/{id}"})
	public ResponseEntity<Endereco> obterEndereco(@PathVariable Long id) {
		Optional<Endereco> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(opt.get(), HttpStatus.OK);
	}

	@PostMapping({"/cadastro", ""})
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		repositorio.save(endereco);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping({"/atualizar", ""})
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		if (atualizacao == null || atualizacao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Endereco> opt = repositorio.findById(atualizacao.getId());
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Endereco endereco = opt.get();
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, atualizacao);
		repositorio.save(endereco);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping({"/{id}", "/excluir/{id}"})
	public ResponseEntity<?> excluirEnderecoPorId(@PathVariable Long id) {
		Optional<Endereco> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Endereco endereco = opt.get();
		List<Cliente> clientes = clienteRepositorio.findAll();
		for (Cliente cliente : clientes) {
			if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(endereco.getId())) {
				cliente.setEndereco(null);
				clienteRepositorio.save(cliente);
			}
		}
		repositorio.delete(endereco);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody(required = false) Endereco exclusao) {
		if (exclusao == null || exclusao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return excluirEnderecoPorId(exclusao.getId());
	}
}

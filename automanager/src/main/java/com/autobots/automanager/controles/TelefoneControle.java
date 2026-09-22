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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@GetMapping({"/telefones", ""})
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		return new ResponseEntity<>(telefones, HttpStatus.OK);
	}

	@GetMapping({"/{id}", "/telefone/{id}"})
	public ResponseEntity<Telefone> obterTelefone(@PathVariable Long id) {
		Optional<Telefone> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(opt.get(), HttpStatus.OK);
	}

	@PostMapping({"/cadastro", ""})
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		repositorio.save(telefone);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping({"/atualizar", ""})
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		if (atualizacao == null || atualizacao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Telefone> opt = repositorio.findById(atualizacao.getId());
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Telefone telefone = opt.get();
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping({"/{id}", "/excluir/{id}"})
	public ResponseEntity<?> excluirTelefonePorId(@PathVariable Long id) {
		Optional<Telefone> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Telefone telefone = opt.get();
		List<Cliente> clientes = clienteRepositorio.findAll();
		for (Cliente cliente : clientes) {
			if (cliente.getTelefones().removeIf(t -> t.getId().equals(id))) {
				clienteRepositorio.save(cliente);
			}
		}
		repositorio.delete(telefone);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody(required = false) Telefone exclusao) {
		if (exclusao == null || exclusao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return excluirTelefonePorId(exclusao.getId());
	}
}

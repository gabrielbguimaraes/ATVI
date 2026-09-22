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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@GetMapping({"/documentos", ""})
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		return new ResponseEntity<>(documentos, HttpStatus.OK);
	}

	@GetMapping({"/{id}", "/documento/{id}"})
	public ResponseEntity<Documento> obterDocumento(@PathVariable Long id) {
		Optional<Documento> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(opt.get(), HttpStatus.OK);
	}

	@PostMapping({"/cadastro", ""})
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		repositorio.save(documento);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping({"/atualizar", ""})
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		if (atualizacao == null || atualizacao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Documento> opt = repositorio.findById(atualizacao.getId());
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Documento documento = opt.get();
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping({"/{id}", "/excluir/{id}"})
	public ResponseEntity<?> excluirDocumentoPorId(@PathVariable Long id) {
		Optional<Documento> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Documento documento = opt.get();
		List<Cliente> clientes = clienteRepositorio.findAll();
		for (Cliente cliente : clientes) {
			if (cliente.getDocumentos().removeIf(d -> d.getId().equals(id))) {
				clienteRepositorio.save(cliente);
			}
		}
		repositorio.delete(documento);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody(required = false) Documento exclusao) {
		if (exclusao == null || exclusao.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return excluirDocumentoPorId(exclusao.getId());
	}
}

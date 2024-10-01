package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import springboot.dto.IO.ListaQuestoesIO;
import springboot.dto.input.ListaQuestoesInput;
import springboot.dto.output.ListaQuestoesOutput;
import springboot.model.ListaQuestoes;
import springboot.model.Usuario;
import springboot.service.ListaQuestoesService;
import springboot.service.UsuarioService;

/**
 * Controlador REST para gerenciamento de listas de questões.
 * Oferece endpoints para operações de CRUD (criação, leitura, atualização,
 * exclusão).
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "+")
public class ListaQuestoesController {

	@Autowired
	private ListaQuestoesService listaQuestoesService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ListaQuestoesIO listaQuestoesIO;

	/**
	 * Converte uma entidade {@link ListaQuestoes} em um DTO
	 * {@link ListaQuestoesOutput}.
	 *
	 * @param lista a lista de questões a ser convertida
	 * @return o DTO de saída correspondente
	 */
	private ListaQuestoesOutput convertToOutput(ListaQuestoes lista) {
		String nomeAutor = usuarioService.getById(lista.getAutor()).getNome();
		return listaQuestoesIO.toDto(lista, nomeAutor);
	}

	/**
	 * Registra uma nova lista de questões no sistema.
	 *
	 * @param usuario       o usuário autenticado que está criando a lista
	 * @param listaQuestoes os dados da lista de questões a ser criada
	 * @return a lista de questões criada
	 */
	@Operation(summary = "Registra uma nova lista de questões no sistema.")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "OK") 
	})
	@PostMapping("/listaquestoes")
	public ResponseEntity<ListaQuestoesOutput> save(@RequestAttribute(name = "usuario") Usuario usuario,
			@RequestBody ListaQuestoesInput listaQuestoes) {
		ListaQuestoes listaSalva = listaQuestoesService
				.save(listaQuestoesIO.toEntity(listaQuestoes, usuario));
		return ResponseEntity.ok(convertToOutput(listaSalva));
	}

	/**
	 * Apaga uma lista de questões do sistema.
	 *
	 * @param id o identificador da lista de questões a ser removida
	 * @return a lista de questões removida
	 */
	@Operation(summary = "Apaga uma lista de questões no sistema.")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "OK") 
	})
	@DeleteMapping("/listaquestoes/{id}")
	public ResponseEntity<ListaQuestoesOutput> delete(@PathVariable("id") String id) {
		ListaQuestoes listaRemovida = listaQuestoesService.delete(id);
		return ResponseEntity.ok(convertToOutput(listaRemovida));
	}

	/**
	 * Atualiza uma lista de questões existente no sistema.
	 *
	 * @param id            o identificador da lista de questões a ser atualizada
	 * @param usuario       o usuário autenticado que está atualizando a lista
	 * @param listaQuestoes os novos dados da lista de questões
	 * @return a lista de questões atualizada
	 */
	@Operation(summary = "Atualiza uma lista de questões do sistema.")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "OK") 
	})
	@PutMapping("/listaquestoes/{id}")
	public ResponseEntity<ListaQuestoesOutput> update(@PathVariable("id") String id,
			@RequestAttribute(name = "usuario") Usuario usuario,
			@RequestBody ListaQuestoesInput listaQuestoes) {
		ListaQuestoes listaAtualizada = listaQuestoesService
				.update(listaQuestoesIO.toEntity(listaQuestoes, usuario), id);
		return ResponseEntity.ok(convertToOutput(listaAtualizada));
	}

	/**
	 * Recupera todas as listas de questões com paginação.
	 *
	 * @param usuario o usuário autenticado
	 * @param page    o número da página a ser recuperada
	 * @param size    o tamanho da página
	 * @return uma página contendo listas de questões
	 */
	@Operation(summary = "Recupera todas as listas de questões com paginação.")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "OK") 
	})
	@GetMapping("/listaquestoes/{page}/{size}")
	public ResponseEntity<Page<ListaQuestoesOutput>> getAll(@RequestAttribute(name = "usuario") Usuario usuario,
			@PathVariable("page") int page,
			@PathVariable("size") int size) {
		Page<ListaQuestoesOutput> pageResult = listaQuestoesService.getAll(usuario, page, size)
				.map(this::convertToOutput);
		return ResponseEntity.ok(pageResult);
	}

	/**
	 * Recupera uma lista de questões específica pelo ID.
	 *
	 * @param id o identificador da lista de questões
	 * @return a lista de questões correspondente
	 */
	@Operation(summary = "Recupera uma lista de questões pelo ID.")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "OK") 
	})
	@GetMapping("/listaquestoes/{id}")
	public ResponseEntity<ListaQuestoesOutput> getById(@PathVariable("id") String id) {
		ListaQuestoes listaQuestoes = listaQuestoesService.getById(id);
		return ResponseEntity.ok(convertToOutput(listaQuestoes));
	}
}

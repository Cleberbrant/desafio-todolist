package com.cleber.desafio_todolist;

import com.cleber.desafio_todolist.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesafioTodolistApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testCreateTodoSucess() {
		Todo todo = Todo.builder()
				.nome("Teste1")
				.descricao("Desc Test1")
				.realizado(false)
				.prioridade(1)
				.build();

	webTestClient
			.post()
			.uri("/todos")
			.bodyValue(todo)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$[0].nome").isEqualTo(todo.getNome())
			.jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
			.jsonPath("$[0].realizado").isEqualTo(todo.isRealizado())
			.jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());
	}

	@Test
	void testCreateTodoFailure() {
		Todo todoFailure = Todo.builder()
				.id(1L)
				.nome("")
				.descricao("")
				.realizado(false)
				.prioridade(0)
				.build();

		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todoFailure)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void testUpdateTodoSucess(){
		Todo existingTodo = Todo.builder()
				.nome("Tarefa Antiga")
				.descricao("Descricao Antiga")
				.realizado(false)
				.prioridade(1)
				.build();

		webTestClient.post()
				.uri("/todos")
				.bodyValue(existingTodo)
				.exchange()
				.expectStatus().isOk();

		Todo updatedTodo = Todo.builder()
				.id(existingTodo.getId()) // O mesmo ID do existente
				.nome("Tarefa Atualizada")
				.descricao("Descrição Atualizada")
				.realizado(true)
				.prioridade(2)
				.build();

		webTestClient.put()
				.uri("/todos") // URI do método de atualização
				.bodyValue(updatedTodo) // Corpo da requisição com os dados atualizados
				.exchange()
				.expectStatus().isOk() // Verifica se retorna 200 OK
				.expectBody() // Valida os campos retornados na resposta
				.jsonPath("$").isArray() // Verifica se é uma lista de todos os Todos
				.jsonPath("$[0].nome").isEqualTo(updatedTodo.getNome()) // Valida o nome atualizado
				.jsonPath("$[0].descricao").isEqualTo(updatedTodo.getDescricao()) // Valida a descrição atualizada
				.jsonPath("$[0].realizado").isEqualTo(updatedTodo.isRealizado()) // Valida se foi marcado como realizado
				.jsonPath("$[0].prioridade").isEqualTo(updatedTodo.getPrioridade()); // Valida a prioridade atualizada

	}
}

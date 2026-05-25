# 🎓 Manual de Estudo da Dupla - Defesa do Sistema Passaporte

Olá, futuros engenheiros! Aqui quem fala é o seu mentor. Fiquem tranquilos se bater aquele desespero, é super normal. O projeto ficou grande, mas a lógica dele é como montar um Lego. 

Eu reescrevi este manual como um **ROTEIRO DE APRESENTAÇÃO**. Siga esta exata ordem na hora de explicar o código para o professor. Se vocês falarem nessa ordem, o raciocínio vai fazer todo o sentido na cabeça da banca.

---

## 🗺️ Passo 1: Por onde começar? (O Banco de Dados e as Classes)

**Na hora da apresentação, comecem abrindo a pasta `com.fateclins.passaporte.model`.**
Digam ao professor: *"Professor, a primeira coisa que nós fizemos foi arquitetar o Banco de Dados. Optamos por usar o banco em memória H2 e o mapeamento ORM do Hibernate, assim o banco nasce automaticamente a partir do nosso código Java."*

Mostrem as classes e expliquem o relacionamento (a Hierarquia):
1. **A Classe `User`:**
   - *"Essa classe representa quem entra no sistema. Ela tem um Enum de `Role` para separar quem é o RH e quem é o CANDIDATO."*
2. **A Classe `PerfilPassaporte`:**
   - *"Esse é o nosso Molde Principal. Imagine que o RH precisa contratar um MOTORISTA. O 'Motorista' é um Perfil. Ele ainda não pertence a nenhuma pessoa, é só a vaga/cargo."*
3. **A Trilha (Hierarquia de `Atividade`, `Tarefa` e `Subtarefa`):**
   - *"Todo perfil exige um passo a passo. Por isso criamos 3 classes conectadas:"*
   - Uma **Atividade** (Ex: Exames Médicos) tem muitas **Tarefas** (Ex: Exame de Vista, Exame de Sangue). *(Mostrem o `@OneToMany` e `@ManyToOne` no código).*
   - Uma **Tarefa** pode ter várias **Subtarefas** (Ex: Agendar data, Entregar laudo).
4. **O Segredo do `isTemplate` (O que tem dentro delas?):**
   - *"Adicionamos um atributo booleano chamado `isTemplate`. Por quê? Porque as atividades cadastradas no cargo 'Motorista' são apenas um MOLDE (`isTemplate = true`). Ninguém está executando elas ainda."*

---

## 🚀 Passo 2: O "Pulo do Gato" (A Classe Passaporte e o Service)

**Agora, abram a classe `Passaporte.java` e depois o `PassaporteService.java`.**

Digam: *"Professor, o coração do nosso sistema é a criação do Passaporte. O Passaporte é a união de uma PESSOA real com o cargo (PERFIL)."*

1. **A Relação do Passaporte:**
   - Mostrem o código do `Passaporte.java`. *"Um Passaporte pertence a 1 Candidato (User) e se baseia em 1 Perfil (PerfilPassaporte)."*
2. **O que acontece quando o RH aperta o botão "Criar Passaporte"?**
   - Abram o `PassaporteService.java` no método `create()`.
   - *"Quando o RH aperta para criar o passaporte do candidato João, o nosso `PassaporteService` entra em ação. Ele faz a 'Mágica da Clonagem'."*
   - *"O Java vai no banco, busca todas as Atividades e Tarefas que eram 'Molde' (isTemplate = true) daquele cargo de Motorista, faz um FOR loop, e **cria cópias exatas** de cada uma delas, mas agora como instâncias reais (`isTemplate = false`) amarradas ao João."*
   - *"É por isso que cada candidato ganha a sua própria lista de exames para fazer sem interferir nos outros."*

---

## 💾 Passo 3: O Armazenamento de Arquivos (Classe Artefato)

**Abram a classe `Artefato.java` e o `ArtefatoController.java`.**

Digam: *"Para a entrega de documentos, não queríamos salvar PDFs soltos na pasta do computador. Queríamos segurança."*

1. **A Classe Artefato:**
   - *"Criamos a classe `Artefato` amarrada às Tarefas. Usamos a anotação `@Lob` (Large Object) e `byte[] dados`. Isso instrui o Hibernate a converter o PDF/Imagem que o usuário manda em números binários e salvar direto na tabela do banco de dados."*
2. **O Controller:**
   - *"No Controller, usamos o `MultipartFile` do Spring. Ele intercepta o arquivo do frontend, extrai os bytes e manda para o banco. Também fizemos um endpoint `DELETE` para caso o usuário queira apagar o arquivo enviado e mandar outro."*

---

## 🤖 Passo 4: A Automação em Cascata (O Diferencial do Projeto)

**Abram a classe `PassaporteTrackingController.java`.**

Digam: *"O backlog pedia que a Atividade fosse atualizada automaticamente com base nas tarefas. Nós fizemos isso no Backend usando requisições REST do tipo PUT."*

- *"Temos um endpoint que muda o status de uma Tarefa (Ex: Quando o RH clica em Validar). Se o RH valida o 'Exame de Sangue', o Java checa se o 'Exame de Vista' também está válido. Se todas as tarefas irmãs estiverem concluídas, o próprio Java **sobe a escada** e marca a 'Atividade' pai como Válida."*
- *"Isso garante que a regra de negócio não fique solta na tela, e sim centralizada e segura na API."*

---

## 🌐 Passo 5: O Segredo do Frontend (O Javascript)

**Por fim, rodem o sistema, abram a página Web, mostrem a tela funcionando e depois abram o `script.js` ou `passaporte.js`.**

Digam: *"Professor, o senhor falou que bastava usar o Postman para as APIs, mas decidimos construir a interface Web integrada usando a arquitetura Single Page Application (SPA) em Javascript puro."*

- **O Fetch:** *"Não usamos `form action` tradicional do HTML. Usamos o `fetch()` assíncrono do Javascript (`async/await`)."*
- **O Passo a Passo de um clique:**
  1. *"Quando o RH aperta para Validar um exame, a página não pisca e não carrega. O JS dispara um `fetch()` por debaixo dos panos para o nosso Controller Java."*
  2. *"O Java faz todo o processamento de cascata no banco de dados e retorna um JSON dizendo 'OK, atualizado'."*
  3. *"O nosso Javascript pega esse JSON e reconstrói o HTML da tabela usando `innerHTML`, trocando a cor das bolinhas de amarelo (Pendente) para verde (Válido) instantaneamente."*

---

## 🛡️ Simulado Rápido de Respostas Prontas

Se o professor perguntar:

1. **"Por que não tem exclusão (DELETE) nas telas de Atividade e Tarefa?"**
   * **Resposta:** *"Porque se excluirmos o 'Exame de Sangue' do meio do molde, isso pode quebrar a trilha de algum candidato que já está no processo. Para segurança e integridade, as atividades do passaporte apenas avançam os status ou são canceladas via banco."*
2. **"Se eu apagar o Passaporte, o banco vai dar erro de Chave Estrangeira com as tarefas vinculadas?"**
   * **Resposta:** *"Daria, se não tivéssemos tratado. No `PassaporteService`, implementamos a deleção em cascata manual: antes de excluir o passaporte, ele busca e apaga os arquivos, as subtarefas e as tarefas, de baixo para cima. O banco fica totalmente limpo."*

---
*Respira fundo. O projeto está excelente, os conceitos estão aplicados corretamente (Camadas MVC/REST, ORM, Fetch, Upload de Blob, Cascade Logic). Vocês vão brilhar! Leia isso com seu parceiro até vocês entenderem a "historinha" que estão contando.*

# sistema-os-convites

Sistema de Ordens de Serviço – Decoração Convites & Artes

Sistema web para organizar os pedidos de convites digitais do meu negócio, que hoje funcionam por WhatsApp e planilhas. A ideia é ter todas as informações de cada pedido em um só lugar, saber o que está perto de vencer e acompanhar os pagamentos.
> **Status:** em planejamento (v0). Este README descreve o que o sistema vai fazer e a ordem em que vou construí-lo.
> 
O problema
Hoje um pedido passa por várias etapas manuais:
O cliente vê o anúncio no Instagram e chama no WhatsApp.
São enviados os modelos, os valores e os formatos (PDF, PNG ou MP4).
O cliente escolhe, paga 50% ou o valor total e envia os dados do convite.
Os dados são copiados para um grupo, a produção é feita e a entrega vai para outro grupo.
O recebimento só é lançado em uma planilha depois, quando dá tempo.
Resultado: os mesmos dados são copiados várias vezes, não há controle de prazos e o financeiro fica atrasado.

Objetivo
Substituir esse fluxo por um sistema onde cada pedido vira uma ordem de serviço (OS) com todos os dados, anexos, prazo e pagamento.

Como vai funcionar
Eu envio ao cliente um link de formulário, em vez de pedir os dados por mensagem.
O cliente preenche os dados do convite e anexa as fotos.
Quem atende anexa a imagem do modelo escolhido, define valor, pagamento e prazo, e a OS é criada.
A tela inicial lista as OS ordenadas pelo prazo (a mais próxima primeiro), mostrando também a data de entrada.
Ao clicar em uma OS, abre a página com o modelo escolhido, as fotos, os dados do cliente e os campos de confirmação.

Funcionalidades
Fase 1 – Base
[ ] Cadastrar OS (cliente, tema, formato, valor, pagamento, prazo)
[ ] Listar OS ordenadas pelo prazo, mostrando a data de entrada
[ ] Página de detalhes da OS
[ ] Alterar status: Novo, Em produção, Pronto, Entregue
Fase 2 – Arquivos
[ ] Upload da imagem do modelo escolhido
[ ] Upload das fotos enviadas pelo cliente
[ ] Exibir o modelo e as fotos na página da OS
Fase 3 – Formulário do cliente
[ ] Link público para o cliente preencher os dados e enviar fotos
[ ] Criação automática da OS a partir do formulário
[ ] Campos: número ou e-mail de confirmação (RSVP) e link do Maps
Fase 4 – Financeiro e acesso
[ ] Controle de pagamento (pendente, sinal de 50% ou total)
[ ] Resumo mensal: pedidos, faturado e recebido
[ ] Alertas de OS atrasadas ou perto do prazo
[ ] Login para os usuários do sistema

Tecnologias
Java 21
Spring Boot
Spring Data JPA / Hibernate
PostgreSQL
Thymeleaf (telas)
Maven
Git e GitHub

Modelo de dados (rascunho)

OrdemServico
|Campo	       - Descrição
|id, numero	   - Identificação da OS
|dataEntrada	 - Quando o pedido entrou
|prazoEntrega	 - Data limite de entrega
|status	       - NOVO, EM_PRODUCAO, PRONTO, ENTREGUE
|clienteNome, clienteWhatsapp, clienteEmail	 - Dados do cliente
|tema, formato - Tema do convite e formato (PDF, PNG, MP4, OUTRO)
|textoConvite	 - Textos e informações do convite
|linkMaps	     - Link da localização do evento
|numeroConfirmacao, emailConfirmacao	 - Contato para confirmação de presença
|valor, statusPagamento	 - Valor total e pagamento (PENDENTE, SINAL_50, TOTAL)
|observacoes   -	Anotações livres

Arquivo

|Campo             - Descrição
|id, nomeOriginal, - caminho	Identificação do arquivo
|tipo	             - MODELO ou FOTO_CLIENTE
|ordemServico      - OS à qual o arquivo pertence


Telas planejadas
Lista de OS: ordenada por prazo, com status, pagamento e dias restantes
Detalhes da OS: modelo, fotos, dados do cliente, confirmação e Maps
Nova OS: formulário interno
Formulário do cliente: página pública para enviar dados e fotos
Painel: resumo do mês

Como rodar
Em construção. As instruções serão adicionadas junto com a primeira versão.

Autora
Raissa Lima – Análise e Desenvolvimento de Sistemas, pós-graduada em Engenharia de Software.

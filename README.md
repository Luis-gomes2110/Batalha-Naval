# Batalha Naval

Jogo de Batalha Naval em Java, feito para a disciplina de Linguagem de Programação (ADS – Fatec).
Seu objetivo é encontrar todas as embarcações escondidas no mar usando o menor número de tiros possível.

## Como jogar

1. **Escolha a dificuldade.** Ao abrir o jogo, uma janela pergunta o nível:
   - **Fácil (6x6):** tabuleiro de 6x6 casas e 30 tiros.
   - **Difícil (8x8):** tabuleiro de 8x8 casas e 40 tiros.
2. **Clique em uma casa** do tabuleiro para atirar. Todas começam escondidas, com a imagem de mar escuro.
3. **Veja o resultado.** Uma mensagem aparece e a casa muda de imagem:
   - **Tiro na água:** a casa mostra o mar claro e você perde um tiro.
   - **Acertou uma embarcação:** a casa mostra uma explosão, e a mensagem diz qual embarcação foi atingida e quantas partes dela já foram acertadas, por exemplo "Acertou o Cruzador! (2/3)".
   - **Embarcação afundada:** quando todas as partes de um navio são atingidas, a explosão é trocada pela imagem dele e a mensagem avisa que ele afundou.
4. **Acompanhe o placar.** Abaixo do tabuleiro ficam o número de **tiros restantes** e quantas **embarcações ainda faltam** afundar.

Todo clique em uma casa nova gasta um tiro, seja na água ou em um navio. Casas que já receberam tiro não podem ser clicadas de novo.

## Como ganhar ou perder

- **Você vence** se acertar todas as partes de todas as embarcações antes que os tiros acabem.
- **Você perde** se os tiros acabarem e ainda restar alguma embarcação. Nesse caso o jogo mostra onde estavam os navios que sobraram.

Ao final da partida, o jogo pergunta se você quer jogar de novo. As embarcações são sorteadas em posições novas a cada partida, então nenhum jogo é igual ao outro.

## As embarcações

| Embarcação | Casas ocupadas | Fácil | Difícil |
| --- | :---: | :---: | :---: |
| Submarino | 1 | 4 | 6 |
| Destroier | 2 | 2 | 2 |
| Cruzador | 3 | 1 | 2 |
| Encouraçado | 4 | 1 | 2 |
| Porta-aviões | 5 | 1 | 1 |

Os navios ficam na horizontal ou na vertical e nunca se sobrepõem. Cada embarcação tem sua própria contagem de acertos, então dois destroieres, por exemplo, afundam de forma independente.

## Dicas

- Navios grandes são mais fáceis de achar. Quando acertar uma parte, atire nas casas ao lado para descobrir se ele está na horizontal ou na vertical.
- Não desperdice tiros em casas coladas a um navio já afundado. Os navios podem se encostar, mas nunca ocupam a mesma casa.
- Os submarinos ocupam uma casa só, então são os mais difíceis de achar. Deixe-os para o final.

## Organização do código

| Arquivo | Função |
| --- | --- |
| `Principal.java` | Mostra a escolha de dificuldade (`JOptionPane`) e inicia o jogo |
| `Embarcacao.java` | Representa uma embarcação: nome, tamanho, acertos e posições |
| `Tabuleiro.java` | Guarda a matriz, sorteia as posições e controla tiros, vitória e derrota |
| `TelaJogo.java` | Janela com a grade de botões, as imagens e o contador de tiros |

As imagens ficam na pasta `imagens/`, que precisa estar ao lado de `src/`.

**Autores:** Matheus e Luís
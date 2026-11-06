package br.com.senac.biblioteca.domain.ports.out;
import java.time.LocalDate;
public interface ClockPort {
    LocalDate dataAtual();
}
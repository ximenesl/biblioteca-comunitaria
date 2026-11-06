package br.com.senac.biblioteca.infrastructure.adapters.out.time;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
@Component
public class SystemClockAdapter implements ClockPort {
    @Override public LocalDate dataAtual() { return LocalDate.now(); }
}
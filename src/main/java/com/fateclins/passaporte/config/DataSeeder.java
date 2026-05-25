package com.fateclins.passaporte.config;

import com.fateclins.passaporte.model.*;
import com.fateclins.passaporte.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PerfilPassaporteRepository perfilRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private SubtarefaRepository subtarefaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco já está populado (se houver usuários, ignoramos o seed)
        if (userRepository.count() > 0) {
            return; // Banco já tem dados
        }

        System.out.println("==================================================");
        System.out.println("🌱 INICIANDO CARGA INICIAL (SEED) DE DADOS MOCKADOS");
        System.out.println("==================================================");

        // 1. Criar um RH
        User rh = new User("rh.carlao", "123456", "Carlão do RH", Role.RH);
        userRepository.save(rh);

        // 2. Criar um Perfil
        PerfilPassaporte perfilMotorista = new PerfilPassaporte("MOTORISTA", "Perfil para condutores da empresa", 1, true);
        perfilMotorista = perfilRepository.save(perfilMotorista);

        // 3. Criar Atividade Molde (Exames Admissionais)
        Atividade exames = new Atividade("Exames Admissionais", "Bateria de exames padrão", 1, perfilMotorista, true);
        exames = atividadeRepository.save(exames);

        // 4. Criar Tarefa 1 (Exame de Sangue)
        Tarefa exameSangue = new Tarefa("Exame de Sangue (Hemograma)", "Coleta no laboratório central", 3, 1, exames, true);
        tarefaRepository.save(exameSangue);

        // 5. Criar Tarefa 2 (Exame Toxicológico) + Suas Subtarefas
        Tarefa toxicologico = new Tarefa("Exame Toxicológico", "Obrigatório para motoristas (Lei Federal)", 5, 2, exames, true);
        toxicologico = tarefaRepository.save(toxicologico);

        Subtarefa coletarFios = new Subtarefa("Realizar coleta na clínica", 1, toxicologico, true);
        subtarefaRepository.save(coletarFios);

        Subtarefa anexarLaudo = new Subtarefa("Entregar laudo impresso ao RH", 2, toxicologico, true);
        subtarefaRepository.save(anexarLaudo);

        System.out.println("✅ Dados MOCKADOS inseridos com sucesso!");
    }
}

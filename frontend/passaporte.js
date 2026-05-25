const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    // Ao abrir a tela, temos 3 missões:
    // 1. Buscar usuários e popular a caixa de seleção do Candidato
    // 2. Buscar perfis e popular a caixa de seleção do Perfil
    // 3. Buscar os passaportes já cadastrados e listar na tabela
    
    loadSelectCandidatos();
    loadSelectPerfis();
    loadPassaportes();

    const form = document.getElementById('passaporte-form');
    form.addEventListener('submit', handleFormSubmit);
});

// =========================================
// PREENCHER DROPDOWN DE CANDIDATOS
// =========================================
async function loadSelectCandidatos() {
    try {
        const response = await fetch(`${API_URL}/users`);
        const users = await response.json();
        
        const select = document.getElementById('candidato-select');
        select.innerHTML = '<option value="" disabled selected>Selecione o Candidato...</option>';

        // O Select só deve exibir usuários que sejam CONTRATADO 
        // (nós filtramos aqui no JS para a interface ficar limpa)
        const candidatos = users.filter(user => user.role === 'CONTRATADO');

        if (candidatos.length === 0) {
            select.innerHTML = '<option value="" disabled selected>Nenhum CONTRATADO encontrado. Crie um lá na tela de Usuários!</option>';
            return;
        }

        candidatos.forEach(user => {
            const option = document.createElement('option');
            option.value = user.id; // O valor que o back-end quer é o ID!
            option.textContent = `#${user.id} - ${user.name} (@${user.username})`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar candidatos', error);
    }
}

// =========================================
// PREENCHER DROPDOWN DE PERFIS
// =========================================
async function loadSelectPerfis() {
    try {
        // Busca a lista de perfis ativos
        const response = await fetch(`${API_URL}/perfis/ativos`);
        const perfis = await response.json();
        
        const select = document.getElementById('perfil-select');
        select.innerHTML = '<option value="" disabled selected>Selecione o Perfil Base...</option>';

        if (perfis.length === 0) {
            // Como não fizemos a tela de Perfis, deixei um "Gato" (Mock) para podermos testar. 
            // Em uma aplicação real, você criaria os perfis na tela deles.
            select.innerHTML = '<option value="1">MOTORISTA 1 (Mockado para o teste)</option>';
            return;
        }

        perfis.forEach(perfil => {
            const option = document.createElement('option');
            option.value = perfil.id; // O valor que o back-end quer é o ID!
            option.textContent = perfil.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar perfis', error);
    }
}

// =========================================
// LISTAR PASSAPORTES
// =========================================
async function loadPassaportes() {
    try {
        const response = await fetch(`${API_URL}/passaportes`);
        const passaportes = await response.json();
        
        const tbody = document.getElementById('passaporte-table-body');
        tbody.innerHTML = ''; 

        if (passaportes.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: var(--text-secondary);">Nenhum passaporte criado.</td></tr>`;
            return;
        }

        passaportes.forEach(p => {
            const tr = document.createElement('tr');
            
            // Formatamos a data que vem do Java (LocalDateTime)
            const dataFormatada = new Date(p.dataCriacao).toLocaleDateString('pt-BR', {
                day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit'
            });

            tr.innerHTML = `
                <td><span style="color: var(--text-secondary)">#${p.id}</span></td>
                <td style="font-weight: 500">${p.candidato.name}</td>
                <td>${p.perfil.nome}</td>
                <td><span class="badge" style="background-color: #fef3c7; color: #92400e;">${p.status}</span></td>
                <td style="font-size: 0.8rem">${dataFormatada}</td>
                <td class="text-right">
                    <button class="btn btn-primary" onclick="openTrackingModal(${p.id})" style="padding: 4px 8px; font-size: 0.8rem; margin-right: 5px;">
                        Ver Detalhes
                    </button>
                    <button class="btn-action-danger" onclick="deletePassaporte(${p.id})">
                        Remover
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Erro:', error);
    }
}

// =========================================
// CRIAR NOVO PASSAPORTE
// =========================================
async function handleFormSubmit(event) {
    event.preventDefault(); 
    
    const submitButton = event.target.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = 'Criando...';
    submitButton.disabled = true;

    // Pegamos OS IDs selecionados nas caixas de seleção
    const candidatoId = document.getElementById('candidato-select').value;
    const perfilId = document.getElementById('perfil-select').value;

    // A MÁGICA: Mandamos um JSON para o Spring dizendo:
    // "Crie um passaporte onde o objeto candidato tem o id X, e o perfil tem o id Y"
    const payload = {
        candidato: { id: candidatoId },
        perfil: { id: perfilId }
    };

    // Obs: Se o perfil do mock foi selecionado e ele não existe no banco, vai dar erro 500. 
    // Precisamos de um Perfil no banco. Vamos fingir que ele existe para a aula.

    try {
        const response = await fetch(`${API_URL}/passaportes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao criar');
        }

        document.getElementById('passaporte-form').reset();
        loadPassaportes(); 
        
    } catch (error) {
        console.error('Erro:', error);
        alert(`Não foi possível salvar: ${error.message}`);
    } finally {
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
    }
}

// =========================================
// DELETAR PASSAPORTE
// =========================================
async function deletePassaporte(id) {
    if (confirm(`Tem certeza que deseja excluir o passaporte #${id}? Essa ação não pode ser desfeita e removerá todo o histórico.`)) {
        try {
            const response = await fetch(`${API_URL}/passaportes/${id}`, { 
                method: 'DELETE' 
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || 'Erro ao deletar passaporte');
            }
            
            loadPassaportes(); 
        } catch (error) {
            console.error('Erro:', error);
            alert(`Não foi possível excluir o passaporte: ${error.message}`);
        }
    }
}

// =========================================
// MODAL DE ACOMPANHAMENTO (O PALCO DA MÁGICA)
// =========================================
async function openTrackingModal(passaporteId) {
    const modal = document.getElementById('tracking-modal');
    const content = document.getElementById('tracking-content');
    modal.style.display = 'block';
    
    // Mostramos o loading
    content.innerHTML = '<p style="color: var(--text-secondary);">Buscando prancheta no Java... 🚀</p>';

    try {
        // Passo 1: O Frontend vai no Endpoint que nós criamos buscar as ATIVIDADES
        const response = await fetch(`${API_URL}/tracking/passaporte/${passaporteId}/atividades`);
        const atividades = await response.json();

        if (atividades.length === 0) {
            content.innerHTML = '<p style="color: var(--text-secondary);">O RH não configurou nenhuma tarefa para o Perfil deste passaporte! O Molde estava vazio na hora que você criou.</p>';
            return;
        }

        let html = '';
        
        // Passo 2: O Javascript desenha cada "Caixinha" de Atividade
        for (const ativ of atividades) {
            html += `
            <div style="border: 1px solid #e5e7eb; padding: 20px; margin-bottom: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
                <h3 style="margin-top: 0; display: flex; align-items: center; justify-content: space-between;">
                    <span>📌 ${ativ.nome}</span> 
                    <span class="badge" style="background-color: #e0e7ff; color: #3730a3; font-size: 0.8rem;">Status: ${ativ.status}</span>
                </h3>
                <p style="color: var(--text-secondary); font-size: 0.9rem; margin-top: -10px; margin-bottom: 15px;">${ativ.descricao}</p>
            `;
            
            // Passo 3: O Frontend busca as TAREFAS (Filhas da Atividade)
            const resTarefas = await fetch(`${API_URL}/tracking/atividade/${ativ.id}/tarefas`);
            const tarefas = await resTarefas.json();
            
            if (tarefas.length > 0) {
                html += `<div style="display: flex; flex-direction: column; gap: 10px;">`;
                for (const tar of tarefas) {
                    html += `
                    <div style="background: #f9fafb; border: 1px solid #f3f4f6; padding: 15px; border-radius: 6px;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                            <strong>✅ ${tar.nome}</strong>
                            <div style="display: flex; align-items: center; gap: 5px;">
                                ${tar.artefatos && tar.artefatos.length > 0 
                                    ? `<button class="btn btn-secondary" style="padding: 2px 6px; font-size: 0.7rem;" onclick="downloadArtefato(${tar.artefatos[0].id})">⬇️ Baixar</button>
                                       <button class="btn-action-danger" style="padding: 2px 6px; font-size: 0.7rem;" onclick="deleteArtefato(${tar.artefatos[0].id}, ${passaporteId})">🗑️</button>`
                                    : `<button class="btn btn-secondary" style="padding: 2px 6px; font-size: 0.7rem;" onclick="triggerUpload('tarefa', ${tar.id}, ${passaporteId})">📎 Anexar</button>`
                                }
                                <span class="badge" style="background-color: ${tar.status === 'VALIDA' ? '#dcfce7' : '#fef3c7'}; color: ${tar.status === 'VALIDA' ? '#166534' : '#92400e'}; font-size: 0.7rem; margin-right: 10px; margin-left: 10px;">${tar.status}</span>
                                <button class="btn btn-primary" style="padding: 2px 6px; font-size: 0.7rem;" onclick="updateStatus('tarefa', ${tar.id}, 'VALIDA', ${passaporteId})">✓</button>
                                <button class="btn-action-danger" style="padding: 2px 6px; font-size: 0.7rem;" onclick="updateStatus('tarefa', ${tar.id}, 'INVALIDA', ${passaporteId})">X</button>
                            </div>
                        </div>
                    `;
                    
                    // Passo 4: O Frontend busca as SUBTAREFAS (Filhas da Tarefa)
                    const resSub = await fetch(`${API_URL}/tracking/tarefa/${tar.id}/subtarefas`);
                    const subtarefas = await resSub.json();
                    
                    if (subtarefas.length > 0) {
                        html += `<ul style="margin: 0; padding-left: 20px; font-size: 0.9rem; color: var(--text-secondary);">`;
                        for (const sub of subtarefas) {
                            html += `
                            <li style="margin-bottom: 5px; display: flex; justify-content: space-between; align-items: center;">
                                <span>📝 ${sub.nome} <em>(${sub.status})</em></span>
                                <div style="display: flex; align-items: center; gap: 5px;">
                                    ${sub.artefatos && sub.artefatos.length > 0 
                                        ? `<button class="btn btn-secondary" style="padding: 2px 6px; font-size: 0.7rem;" onclick="downloadArtefato(${sub.artefatos[0].id})">⬇️ Baixar</button>
                                           <button class="btn-action-danger" style="padding: 2px 6px; font-size: 0.7rem;" onclick="deleteArtefato(${sub.artefatos[0].id}, ${passaporteId})">🗑️</button>`
                                        : `<button class="btn btn-secondary" style="padding: 2px 6px; font-size: 0.7rem;" onclick="triggerUpload('subtarefa', ${sub.id}, ${passaporteId})">📎 Anexar</button>`
                                    }
                                    <button class="btn btn-primary" style="padding: 2px 6px; font-size: 0.7rem; margin-left: 10px;" onclick="updateStatus('subtarefa', ${sub.id}, 'VALIDA', ${passaporteId})">✓</button>
                                    <button class="btn-action-danger" style="padding: 2px 6px; font-size: 0.7rem;" onclick="updateStatus('subtarefa', ${sub.id}, 'INVALIDA', ${passaporteId})">X</button>
                                </div>
                            </li>`;
                        }
                        html += `</ul>`;
                    }
                    html += `</div>`; // Fim Tarefa
                }
                html += `</div>`; // Fim grupo tarefas
            } else {
                html += `<p style="font-size: 0.9rem; color: var(--text-secondary);">Sem tarefas cadastradas.</p>`;
            }
            html += `</div>`; // Fim Atividade
        }
        
        content.innerHTML = html;
        
    } catch (error) {
        content.innerHTML = `<p style="color: red;">Erro ao carregar acompanhamento: ${error.message}</p>`;
    }
}

// Fechar Modal
function closeTrackingModal() {
    document.getElementById('tracking-modal').style.display = 'none';
}

// =========================================
// ATUALIZAR STATUS DE TAREFA/SUBTAREFA
// =========================================
async function updateStatus(tipo, id, novoStatus, passaporteId) {
    if (!confirm(`Deseja alterar o status para ${novoStatus}?`)) return;

    try {
        const response = await fetch(`${API_URL}/tracking/${tipo}/${id}/status/${novoStatus}`, {
            method: 'PUT'
        });

        if (!response.ok) {
            throw new Error('Falha ao atualizar o status');
        }

        // Recarrega o modal para mostrar a cor verde/vermelha
        openTrackingModal(passaporteId);

    } catch (error) {
        console.error('Erro:', error);
        alert(`Não foi possível atualizar o status: ${error.message}`);
    }
}

// =========================================
// UPLOAD E DOWNLOAD DE ARTEFATOS
// =========================================
function triggerUpload(tipo, id, passaporteId) {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.onchange = e => {
        const file = e.target.files[0];
        if (file) {
            uploadArtefato(tipo, id, file, passaporteId);
        }
    };
    fileInput.click();
}

async function uploadArtefato(tipo, id, file, passaporteId) {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(`${API_URL}/artefatos/${tipo}/${id}`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Falha no upload do arquivo');
        }

        alert('Arquivo anexado com sucesso!');
        openTrackingModal(passaporteId); // Recarrega para mostrar o botão de baixar
    } catch (error) {
        console.error('Erro no upload:', error);
        alert(`Erro ao anexar arquivo: ${error.message}`);
    }
}

function downloadArtefato(artefatoId) {
    window.open(`${API_URL}/artefatos/download/${artefatoId}`, '_blank');
}

async function deleteArtefato(artefatoId, passaporteId) {
    if (!confirm('Deseja realmente excluir este arquivo?')) return;
    
    try {
        const response = await fetch(`${API_URL}/artefatos/${artefatoId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Falha ao excluir o arquivo');
        }

        openTrackingModal(passaporteId); // Recarrega para mostrar o botão de anexar novamente
    } catch (error) {
        console.error('Erro ao excluir:', error);
        alert(`Erro: ${error.message}`);
    }
}

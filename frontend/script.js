const API_URL = 'http://localhost:8080/api';

// Executado quando a página terminar de carregar
document.addEventListener('DOMContentLoaded', () => {
    loadUsuarios();

    const form = document.getElementById('user-form');
    form.addEventListener('submit', handleFormSubmit);
});

// =========================================
// BUSCAR E LISTAR USUÁRIOS (READ)
// =========================================
async function loadUsuarios() {
    try {
        // 1. Faz a requisição GET para o Backend
        const response = await fetch(`${API_URL}/users`);
        
        if (!response.ok) throw new Error('Falha ao buscar dados');
        
        // 2. Converte a resposta em JSON
        const users = await response.json();
        
        // 3. Monta o HTML da tabela
        const tbody = document.getElementById('user-table-body');
        tbody.innerHTML = ''; // Limpa a tabela antes de preencher

        if (users.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: var(--text-secondary);">Nenhum usuário cadastrado.</td></tr>`;
            return;
        }

        users.forEach(user => {
            const tr = document.createElement('tr');
            
            // Formatamos a role para minusculo para aplicar a classe CSS correta (ex: role-rh)
            const roleClass = `role-${user.role.toLowerCase()}`;
            
            // Tratamento especial para nomes bonitos na Badge
            const roleName = user.role.replace('_', ' ');

            tr.innerHTML = `
                <td><span style="color: var(--text-secondary)">#${user.id}</span></td>
                <td style="font-weight: 500">${user.name}</td>
                <td>${user.username}</td>
                <td><span class="badge ${roleClass}">${roleName}</span></td>
                <td class="text-right">
                    <button class="btn-action-danger" onclick="deleteUser(${user.id}, '${user.name}')">
                        Remover
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Erro:', error);
        alert('Não foi possível carregar os usuários. Verifique se o Back-end está rodando.');
    }
}

// =========================================
// CRIAR NOVO USUÁRIO (CREATE)
// =========================================
async function handleFormSubmit(event) {
    event.preventDefault(); // Impede a página de recarregar
    
    const submitButton = event.target.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    
    // Mostra um "Carregando" no botão
    submitButton.innerHTML = 'Salvando...';
    submitButton.disabled = true;

    // Pega os dados dos inputs
    const name = document.getElementById('name').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const payload = {
        name: name,
        username: username,
        password: password,
        role: role
    };

    try {
        // Envia o POST com o JSON no Body
        const response = await fetch(`${API_URL}/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            // Se o backend retornou erro (ex: 400 Bad Request por causa de login repetido)
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao criar usuário');
        }

        // Se deu tudo certo
        document.getElementById('user-form').reset(); // Limpa o formulário
        loadUsuarios(); // Recarrega a tabela para mostrar o novo usuário
        
    } catch (error) {
        console.error('Erro:', error);
        alert(`Não foi possível salvar: ${error.message}`);
    } finally {
        // Volta o botão ao normal
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
    }
}

// =========================================
// DELETAR USUÁRIO (DELETE)
// =========================================
async function deleteUser(id, name) {
    if (confirm(`Tem certeza que deseja excluir o usuário ${name}? Essa ação não pode ser desfeita.`)) {
        try {
            const response = await fetch(`${API_URL}/users/${id}`, { 
                method: 'DELETE' 
            });
            
            if (!response.ok) throw new Error('Erro ao deletar');
            
            loadUsuarios(); // Atualiza a tela
        } catch (error) {
            console.error('Erro:', error);
            alert('Não foi possível excluir o usuário.');
        }
    }
}

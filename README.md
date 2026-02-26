# Lara - Gestão de Carga Mental Doméstica 🌿

Lara é um aplicativo Android nativo desenvolvido em Kotlin e Jetpack Compose, focado em reduzir a carga mental doméstica através de uma abordagem empática e minimalista.

## 🚀 Como usar

1. **Gere sua API Key**:
   - Acesse o [Google AI Studio](https://aistudio.google.com/).
   - Clique em "Get API Key".
   - Copie a chave gerada.

2. **Configuração Inicial**:
   - Ao abrir o app pela primeira vez, insira a sua `GEMINI_API_KEY`.
   - A chave será salva de forma segura no seu dispositivo usando `EncryptedSharedPreferences`.

3. **Onboarding**:
   - Conte para a Lara sobre sua casa (cômodos, pets, crianças e eletrodomésticos).
   - A Lara usará o modelo Gemini 1.5 Flash para criar uma rotina inicial personalizada e equilibrada.

4. **Dashboard "Foco de Hoje"**:
   - Veja apenas o essencial (máximo de 5 tarefas).
   - Marque como concluído para ver a animação de sucesso.

5. **Botão Imprevisto**:
   - Algo aconteceu? Clique no botão de imprevisto e conte para a Lara.
   - Ela reorganizará seu dia priorizando o seu descanso e o que é realmente vital.

## 🛠 Stack Técnica

- **Linguagem**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Banco de Dados**: Room Database (Offline-first)
- **IA**: Google Generative AI SDK (Gemini API)
- **Segurança**: Android Security Crypto (EncryptedSharedPreferences)
- **Arquitetura**: MVVM (Model-View-ViewModel)

## 🎨 Design System

- **Fundo**: `#FDFCF0` (Creme relaxante)
- **Sucesso**: `#A8D5BA` (Verde menta)
- **Urgência Suave**: `#FFB7B2` (Coral pastel)
- **Tipografia**: Focada em legibilidade e acolhimento.

---
*Desenvolvido com carinho para tornar sua vida mais leve.*

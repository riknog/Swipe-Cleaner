# Guia de Contribuição — SwipeCleaner

Obrigado pelo seu interesse em contribuir com o **SwipeCleaner**! Para manter a qualidade, a segurança e a organização do código, siga as diretrizes abaixo.

---

## 🚀 Como Contribuir

### 1. Padrão de Branches
Crie sempre uma branch a partir da `main` seguindo a convenção:
- `feat/nome-da-funcionalidade` (para novas features)
- `fix/descricao-da-correcao` (para correção de bugs)
- `docs/nome-da-documentacao` (para documentação)

### 2. Padrão de Commits
Siga o padrão prefixado de commits:
- `Feat: adicionado suporte a novas animações nos cards`
- `Fix: corrigido erro de vazamento de memória ao carregar fotos`
- `BugFix: corrigida exceção de permissão no Android 14`
- `Docs: atualizado guia de contribuição`

### 3. Validação Local Obrigatória
Antes de enviar seu Pull Request, certifique-se de que o código compila e passa nos testes unitários:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

---

## 🔒 Segurança e Regras

1. **Nunca envie chaves de API, senhas ou segredos** nos commits.
2. **Não altere arquivos de configuração central** (`.github/`, `build.gradle.kts`) sem alinhamento prévio nas Issues.
3. Todo código enviado deve passar por aprovação via **Pull Request**. Push direto na branch `main` é bloqueado por segurança.

---

## 📋 Processo de Pull Request (PR)

1. Preencha o template de Pull Request por completo.
2. Aguarde a validação automatizada do **GitHub Actions (Android CI)**.
3. Um revisor analisará seu código. Faça os ajustes solicitados se necessário.

# 📲 SwipeCleaner - Exclusão Rápida e Segura de Fotos da Galeria

> **Organize sua galeria Android com deslizes no estilo Tinder!**  
> Limpe fotos duplicadas ou indesejadas de forma simples, rápida e com **100% de segurança contra exclusões acidentais**.

---

## 🌟 Destaques do Aplicativo

- 🎴 **Interface Estilo Tinder (Card Swiper)**:
  - **Deslize para a Esquerda (🔴)**: Mova a foto para a Lixeira.
  - **Deslize para a Direita (🟢)**: Mantenha a foto na sua galeria.
  - **Deslize para Cima (⭐)**: Marque a foto como Favorita.
- 🛡️ **Lixeira de Segurança com Dupla Camada**:
  - Nenhuma imagem é apagada do armazenamento do seu dispositivo imediatamente.
  - Todas as fotos descartadas ficam salvas na aba **Lixeira de Segurança**.
  - Você pode revisar o tamanho total acumulado de espaço a liberar e **restaurar qualquer foto a qualquer momento com apenas 1 toque**.
- ↩️ **Botão Desfazer (Undo)**:
  - Tomou a decisão errada ao deslizar? O botão **Desfazer** reverte instantaneamente sua última ação.
- 🖼️ **Acesso Direto à Galeria Real (MediaStore API)**:
  - Lê dinamicamente as fotos reais do armazenamento interno ou cartão SD através das permissões nativas `READ_MEDIA_IMAGES` / `READ_EXTERNAL_STORAGE`.
  - Caso o dispositivo não possua fotos ou a permissão não tenha sido concedida ainda, o app conta com modo de demonstração interativo.

---

## 📸 Como Funciona

```
      [ ⬆️ Favoritar (Cima) ]
                 │
 [ 🔴 Excluir ] ──┼── [ 🟢 Manter ]
   (Esquerda)    │     (Direita)
                 │
      [ ↩️ Desfazer (Undo) ]
```

---

## 🛠️ Tecnologias Utilizadas

Este projeto foi desenvolvido seguindo os padrões modernos do ecossistema Android:

- **Linguagem**: [Kotlin](https://kotlinlang.org/)
- **Interface Gráfica**: [Jetpack Compose](https://developer.android.com/jetpack/compose) com Material Design 3 (M3)
- **Persistência Local**: [Room Database](https://developer.android.com/training/data-storage/room) com KSP (Kotlin Symbol Processing)
- **Carregamento de Imagens**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Acesso ao Armazenamento**: Android `MediaStore` Content Resolver API
- **Arquitetura**: MVVM (Model-View-ViewModel) + Kotlin Flow & Coroutines
- **Integração Contínua (CI)**: GitHub Actions

---

## 🚀 Como Compilar e Rodar

### Pré-requisitos
- **Android Studio** Ladybug (ou versão superior)
- **JDK 17** pré-instalado
- Dispositivo Android físico ou Emulador (Android 7.0 / API 24+)

### Passos
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/SEU_USUARIO/SwipeCleaner.git
   cd SwipeCleaner
   ```

2. **Abra no Android Studio**:
   - Escolha **Open an existing project** e selecione a pasta do projeto.

3. **Execute os testes unitários**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Gere o APK de Debug ou Instale no Aparelho**:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🧪 Testes e Qualidade de Código

O projeto conta com uma suíte de testes automatizados para validação contínua:

- **Testes Unitários**: Validação dos cálculos de tamanho de arquivo (`formatBytes`), estados de ação do `PhotoItem` e gerenciamento de pilhas de desfazer.
- **Testes de Integração com Robolectric**: Verificação de recursos do contexto Android.
- **GitHub Actions Workflows**: Pipeline de CI configurado em `.github/workflows/android-build.yml` que executa builds e testes a cada `push` e `pull_request`.

---

## 🔒 Segurança e Privacidade

- **Privacidade do Usuário**: Nenhuma foto ou informação da sua galeria é enviada para servidores externos ou nuvem. Todo o processamento e indexação de dados são **100% locais** no seu aparelho.
- **Segurança de Armazenamento**: O comando final de remoção da Lixeira solicita confirmação prévia com o resumo do espaço a ser liberado.

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

applyTo:
  - src/main/java/**
  - src/test/java/**
  - build.gradle
  - settings.gradle
---

# バックエンド開発用カスタム命令

## コーディング規約
- Javaのコーディング規約に従い、適切な命名規則を使用する
- Spring Bootのベストプラクティスに準拠する
- Lombokアノテーションを積極的に活用する（@Data, @NoArgsConstructor, @AllArgsConstructor等）
- Springアノテーションを適切に使用する（@Service, @Controller, @Repository等）

## アーキテクチャ
- MVC パターンを維持する（Controller - Service - Repository）
- DIを適切に使用し、@Autowiredよりもコンストラクタインジェクションを推奨
- RESTful APIの設計原則に従う
- 1コントローラー1エンドポイントを基本とする

## コード品質
- メソッドは単一責任の原則に従い、簡潔に保つ
- 適切な例外処理を実装する
- ログ出力を適切に配置する（SLF4Jを使用）

## テスト
- 単体テストの作成を推奨
- @SpringBootTestを使用した統合テストを適切に実装
- テストメソッド名は日本語でも可（テストの意図が明確になる場合）

## 依存関係
- 新しい依存関係を追加する際は、必要性を検討し、最小限に留める
- Spring Boot Starterを優先的に使用する

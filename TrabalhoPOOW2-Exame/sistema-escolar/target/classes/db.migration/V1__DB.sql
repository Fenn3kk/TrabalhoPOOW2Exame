CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Criando a tabela Administrador
CREATE TABLE Administrador (
                               id SERIAL PRIMARY KEY,
                               uuid UUID UNIQUE NOT NULL,
                               nome VARCHAR(100) NOT NULL,
                               senha VARCHAR(100) NOT NULL,
                               data_cadastro TIMESTAMP NOT NULL,
                               permissao VARCHAR(20) NOT NULL
);

-- Criando a tabela Aluno
CREATE TABLE Aluno (
                       id SERIAL PRIMARY KEY,
                       uuid UUID UNIQUE NOT NULL,
                       nome VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL
);

-- Criando a tabela Turma
CREATE TABLE Turma (
                       id SERIAL PRIMARY KEY,
                       uuid UUID UNIQUE NOT NULL,
                       nome VARCHAR(100) NOT NULL
);

-- Criando a tabela Disciplina
CREATE TABLE Disciplina (
                            id SERIAL PRIMARY KEY,
                            uuid UUID UNIQUE NOT NULL,
                            nome VARCHAR(100) NOT NULL
);

-- Criando a tabela Professor
CREATE TABLE Professor (
                           id SERIAL PRIMARY KEY,
                           uuid UUID UNIQUE NOT NULL,
                           nome VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL
);

-- Tabela intermediária para muitos-para-muitos entre Aluno e Turma
CREATE TABLE Aluno_turma
(
                            id_turma  INT NOT NULL,
                            id_aluno INT NOT NULL,
                            PRIMARY KEY (id_turma, id_aluno),
                            FOREIGN KEY (id_turma) REFERENCES Turma (id) ON DELETE CASCADE,
                            FOREIGN KEY (id_aluno) REFERENCES Aluno (id) ON DELETE CASCADE
);

-- Tabela intermediária para muitos-para-muitos entre Professor e Disciplina
CREATE TABLE Professor_disciplina (
                            id_professor INT NOT NULL,
                            id_disciplina INT NOT NULL,
                            PRIMARY KEY (id_professor, id_disciplina),
                            FOREIGN KEY (id_professor) REFERENCES Professor(id) ON DELETE CASCADE,
                            FOREIGN KEY (id_disciplina) REFERENCES Disciplina(id) ON DELETE CASCADE
);

-- Criando a tabela Horario (tabela de junção com atributos adicionais)
CREATE TABLE Horario (
                         id_horario SERIAL PRIMARY KEY,
                         uuid_horario UUID UNIQUE NOT NULL,
                         id_turma INT,
                         id_professor INT,
                         id_disciplina INT,
                         dia_horario VARCHAR(100),
                         hora_horario VARCHAR(100),
                         FOREIGN KEY (id_turma) REFERENCES Turma(id) ON DELETE CASCADE,
                         FOREIGN KEY (id_professor) REFERENCES Professor(id) ON DELETE CASCADE,
                         FOREIGN KEY (id_disciplina) REFERENCES Disciplina(id) ON DELETE CASCADE,
                         CONSTRAINT unique_turma_professor_disciplina UNIQUE (id_turma, id_professor, id_disciplina)
);
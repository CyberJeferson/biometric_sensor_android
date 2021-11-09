-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 07-Nov-2021 às 20:46
-- Versão do servidor: 10.4.14-MariaDB
-- versão do PHP: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `aps`
--

DELIMITER $$
--
-- Procedimentos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `createTaxe` (IN `jobID` VARCHAR(30), IN `nome` VARCHAR(30), IN `receitaTrimestral` DOUBLE)  BEGIN
    declare finalIRPJ, finalAddIRPJ, ImpostolucroPrsumido, totaladdIR , finalCSLL , totalCSLL, finalPIS,finalCOFINS,totalTaxas  DOUBLE DEFAULT 0.0;
	
    
    set finalIRPJ = (receitaTrimestral * 8.0) / 100.0;
    set ImpostolucroPrsumido = (finalIRPJ * 15.0) / 100.0;
	
	
    set finalCSLL = (receitaTrimestral * 12.0) / 100.0;
    set totalCSLL = (finalCSLL * 9.0) / 100.0;
    set finalPIS = (receitaTrimestral * 1.34) / 100.0;
    set finalCOFINS = (receitaTrimestral * 3.0) / 100.0;
    
    IF  finalIRPJ >= 60000.0  THEN
		set  finalAddIRPJ = ((finalIRPJ - 60000.0) * 10.0) / 100.0;
		set	totaladdIR = ImpostolucroPrsumido - finalAddIRPJ;
                 
    END IF;	
	set totalTaxas = ImpostolucroPrsumido + totaladdIR + totalCSLL + finalPIS + finalCOFINS + jobID;
	insert into inf_taxes(taxe_name,taxe_value,taxe_IR,taxe_adicionalIR,taxe_CSLL, taxe_PIS,taxe_COFINS,taxe_job_id,taxe_totalTaxes) values(nome, receitaTrimestral, ImpostolucroPrsumido,totaladdIR,totalCSLL,finalPIS,finalCOFINS,jobID,totalTaxas);
    
    
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `createTrib` (IN `INFID` INT, IN `nome` VARCHAR(30), IN `ValorProdutosemIPI` DOUBLE)  BEGIN
declare IRPF, CSLL, PIS, COFINS, ICMS, IPI, TOTALTRIBUTOS DOUBLE DEFAULT 0.0;
declare INSS DOUBLE DEFAULT 5.55; -- INSS é calculado com 2,85% sobre a receita bruta e 2,70% sobre a  remuneração.
declare destino varchar(30);
select private_infos.inf_dest_product into destino from private_infos where private_infos.inf_id LIKE INFID;
IF destino = 'MERCADO' THEN -- SE FOR PARA O MERCADO INTERNO
set IRPF = (((ValorProdutosemIPI * 8.0) / 100.0) * 15.0) / 100;
set CSLL = (ValorProdutosemIPI * 12.0) / 100.0;
set PIS = (ValorProdutosemIPI * 1.34) / 100.0;
set COFINS = (ValorProdutosemIPI * 3.0) / 100.0;
set ICMS = (ValorProdutosemIPI * 12.0) / 100.0;
set IPI = (ValorProdutosemIPI *  15.0) / 100.0;
set INSS = (ValorProdutosemIPI * INSS) / 100.0;
ELSE -- EXPORTAÇÃO
set IRPF = (((ValorProdutosemIPI * 8.0) / 100.0) * 15.0) / 100;
set CSLL = (ValorProdutosemIPI * 12.0) / 100.0;
set INSS = (ValorProdutosemIPI * INSS) / 100.0;
set PIS = 0.0; -- ISENTO
set COFINS = 0.0; -- ISENTO
set ICMS = 0.0; -- ISENTO
set IPI = 0.0; -- ISENTO
END IF;
set TOTALTRIBUTOS =  IRPF + CSLL + PIS + COFINS + ICMS + IPI; -- SOMA DE TODAS AS TAXAS SOBRE O VALOR DO PRODUTO
-- INSERINDO TODAS INFORMAÇÕES NO BANCO DE DADOS.
insert into taxation(taxation_name,taxation_valueprod,taxation_IRPF,taxation_CSLL,taxation_PIS,taxation_COFINS,taxation_ICMS,taxation_IPI,taxation_INSS,taxation_TOTAL,taxation_infID) values(nome, ValorProdutosemIPI,IRPF,CSLL,PIS,COFINS,ICMS,IPI,INSS,TOTALTRIBUTOS,INFID);

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `biohazard_pesticide`
--

CREATE TABLE `biohazard_pesticide` (
  `bio_id` int(11) NOT NULL,
  `bio_name` varchar(30) NOT NULL,
  `bio_prohibited` tinyint(1) NOT NULL,
  `bio_jobid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `biohazard_pesticide`
--

INSERT INTO `biohazard_pesticide` (`bio_id`, `bio_name`, `bio_prohibited`, `bio_jobid`) VALUES
(1, 'ALDRIM 309-00-2', 1, 1),
(2, 'LINDANO 58-89-9', 1, 1),
(3, 'METAMIDOFOS 10265-92-6', 1, 1),
(4, 'ENDOSULFAN 115-29-7', 1, 1),
(5, 'PARATIONA METILICA 298-00-0', 0, 1),
(6, 'ALDRIM ', 1, 2),
(7, 'MONOCROTOFÓS', 1, 2),
(8, 'METAMIDOFÓS', 1, 2),
(9, 'PARATIONA METÍLICA', 1, 2),
(10, 'CARBENDAZIM', 0, 2),
(11, 'BOSCALIDA', 0, 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `inf_taxes`
--

CREATE TABLE `inf_taxes` (
  `taxe_id` int(11) NOT NULL,
  `taxe_name` varchar(30) NOT NULL,
  `taxe_value` double NOT NULL,
  `taxe_job_id` int(11) NOT NULL,
  `taxe_IR` double NOT NULL,
  `taxe_adicionalIR` double NOT NULL,
  `taxe_CSLL` double NOT NULL,
  `taxe_PIS` double NOT NULL,
  `taxe_COFINS` double NOT NULL,
  `taxe_totalTaxes` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `inf_taxes`
--

INSERT INTO `inf_taxes` (`taxe_id`, `taxe_name`, `taxe_value`, `taxe_job_id`, `taxe_IR`, `taxe_adicionalIR`, `taxe_CSLL`, `taxe_PIS`, `taxe_COFINS`, `taxe_totalTaxes`) VALUES
(1, 'TRIMESTRE 25/03/2008', 50000, 1, 600, 0, 540, 670, 1500, 3311),
(2, 'TRIMESTRE 25/06/2008', 3000000.75, 1, 36000.009, 18000.002999999997, 32400.0081, 40200.010050000004, 90000.0225, 216601.05265000003),
(3, 'TRIMESTRE 25/09/2008', 2000000, 1, 24000, 14000, 21600, 26800, 60000, 146401),
(4, 'TRIMESTRE 25/12/2008', 3000000.55, 1, 36000.0066, 18000.002200000003, 32400.005939999995, 40200.00737, 90000.01649999998, 216601.03861),
(5, 'TRIMESTRE 25/03/2009', 1000000.4, 1, 12000.0048, 10000.0016, 10800.00432, 13400.005360000001, 30000.012000000002, 76201.02808),
(6, 'TRIMESTRE 25/06/2009', 5000000.35, 1, 60000.004199999996, 26000.0014, 54000.003779999985, 67000.00469, 150000.01049999997, 357001.02457),
(7, 'TRIMESTRE 25/09/2009', 900000.45, 1, 10800.005399999998, 9600.001799999998, 9720.00486, 12060.00603, 27000.013499999997, 69181.03159),
(9, 'TRIMESTRE 25/06/2008', 5000000.75, 2, 60000.009000000005, 26000.003000000004, 54000.0081, 67000.01005000001, 150000.0225, 357002.05265),
(10, 'TRIMESTRE 25/09/2008', 5000000, 2, 60000, 26000, 54000, 67000, 150000, 357002),
(11, 'TRIMESTRE 25/12/2008', 4000000.55, 2, 48000.0066, 22000.002200000003, 43200.005939999995, 53600.00737, 120000.01649999998, 286802.03861),
(12, 'TRIMESTRE 25/03/2009', 4000000.4, 2, 48000.0048, 22000.0016, 43200.00432, 53600.00536, 120000.01199999999, 286802.02807999996),
(13, 'TRIMESTRE 25/06/2009', 3000000.35, 2, 36000.0042, 18000.0014, 32400.00378, 40200.00469, 90000.0105, 216602.02457),
(14, 'TRIMESTRE 25/09/2009', 900000.45, 2, 10800.005399999998, 9600.001799999998, 9720.00486, 12060.00603, 27000.013499999997, 69182.03159),
(15, 'TRIMESTRE 25/06/2008', 5000000.75, 3, 60000.009000000005, 26000.003000000004, 54000.0081, 67000.01005000001, 150000.0225, 357003.05265),
(16, 'TRIMESTRE 25/09/2008', 5000000, 3, 60000, 26000, 54000, 67000, 150000, 357003),
(17, 'TRIMESTRE 25/12/2008', 4000000.55, 3, 48000.0066, 22000.002200000003, 43200.005939999995, 53600.00737, 120000.01649999998, 286803.03861),
(18, 'TRIMESTRE 25/03/2009', 4000000.4, 3, 48000.0048, 22000.0016, 43200.00432, 53600.00536, 120000.01199999999, 286803.02807999996),
(19, 'TRIMESTRE 25/06/2009', 3000000.35, 3, 36000.0042, 18000.0014, 32400.00378, 40200.00469, 90000.0105, 216603.02457),
(20, 'TRIMESTRE 25/09/2009', 900000.45, 3, 10800.005399999998, 9600.001799999998, 9720.00486, 12060.00603, 27000.013499999997, 69183.03159),
(21, 'TRIMESTRE 25/06/2008', 5000000.75, 4, 60000.009000000005, 26000.003000000004, 54000.0081, 67000.01005000001, 150000.0225, 357004.05265),
(22, 'TRIMESTRE 25/09/2008', 5000000, 4, 60000, 26000, 54000, 67000, 150000, 357004),
(23, 'TRIMESTRE 25/12/2008', 4000000.55, 4, 48000.0066, 22000.002200000003, 43200.005939999995, 53600.00737, 120000.01649999998, 286804.03861),
(24, 'TRIMESTRE 25/03/2009', 4000000.4, 4, 48000.0048, 22000.0016, 43200.00432, 53600.00536, 120000.01199999999, 286804.02807999996),
(25, 'TRIMESTRE 25/06/2009', 3000000.35, 4, 36000.0042, 18000.0014, 32400.00378, 40200.00469, 90000.0105, 216604.02457),
(26, 'TRIMESTRE 25/09/2009', 900000.45, 4, 10800.005399999998, 9600.001799999998, 9720.00486, 12060.00603, 27000.013499999997, 69184.03159),
(27, 'TRIMESTRE 25/06/2008', 5000000.75, 5, 60000.009000000005, 26000.003000000004, 54000.0081, 67000.01005000001, 150000.0225, 357005.05265),
(28, 'TRIMESTRE 25/09/2008', 5000000, 5, 60000, 26000, 54000, 67000, 150000, 357005),
(29, 'TRIMESTRE 25/12/2008', 4000000.55, 5, 48000.0066, 22000.002200000003, 43200.005939999995, 53600.00737, 120000.01649999998, 286805.03861),
(30, 'TRIMESTRE 25/03/2009', 4000000.4, 5, 48000.0048, 22000.0016, 43200.00432, 53600.00536, 120000.01199999999, 286805.02807999996),
(31, 'TRIMESTRE 25/06/2009', 3000000.35, 5, 36000.0042, 18000.0014, 32400.00378, 40200.00469, 90000.0105, 216605.02457),
(32, 'TRIMESTRE 25/09/2009', 900000.45, 5, 10800.005399999998, 9600.001799999998, 9720.00486, 12060.00603, 27000.013499999997, 69185.03159);

-- --------------------------------------------------------

--
-- Estrutura da tabela `job_role`
--

CREATE TABLE `job_role` (
  `id` int(11) NOT NULL,
  `ds` varchar(90) NOT NULL,
  `job_level` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `job_role`
--

INSERT INTO `job_role` (`id`, `ds`, `job_level`) VALUES
(1, 'MINISTRO DO MEIO AMBIENTE', 3),
(2, 'DIRETOR DE DIVISÕES', 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `private_infos`
--

CREATE TABLE `private_infos` (
  `inf_id` int(11) NOT NULL,
  `inf_name` varchar(30) NOT NULL,
  `inf_file` varchar(500) NOT NULL,
  `inf_ address` varchar(100) NOT NULL,
  `inf_products` varchar(30) NOT NULL,
  `inf_ann_production` int(11) NOT NULL,
  `inf_dest_product` varchar(30) NOT NULL,
  `inf_numb_employees` int(11) NOT NULL,
  `inf_qtd_machines` int(11) NOT NULL,
  `inf_aut_lvl` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `private_infos`
--

INSERT INTO `private_infos` (`inf_id`, `inf_name`, `inf_file`, `inf_ address`, `inf_products`, `inf_ann_production`, `inf_dest_product`, `inf_numb_employees`, `inf_qtd_machines`, `inf_aut_lvl`) VALUES
(1, 'SEU ZÉ PRODUTOR DE MILHO SA', 'https://image.flaticon.com/icons/png/512/1256/1256434.png', '', 'MILHO', 1000, 'MERCADO', 60000, 1000, 5),
(2, 'PRODUÇÃO DE FEIJÃO LTDA', 'https://cdn-icons-png.flaticon.com/512/5235/5235310.png', 'GO Goiás Alexânia 331 Fazenda São José 1 - JOSE LUCENA DANTAS -\r\n00016985168\r\n', 'FEIÃO', 4000, 'MERCADO', 15, 300, 5),
(3, 'TOMATITOS DO CÉU EIRELI', 'https://cdn-icons-png.flaticon.com/512/2909/2909922.png', 'MT Mato Grosso Juscimeira 32131\r\nFAZENDA SANTO\r\nANTONIO DO OURO\r\nBRANCO\r\n13233128-4 - ANTONIO CARLOS ', 'TOMATE', 5000, 'MERCADO', 30000, 400, 4),
(4, 'MUNDO 3D MEI', 'https://cdn-icons-png.flaticon.com/512/1051/1051622.png', 'SP São Paulo Pereira Barreto 13133 Fazenda Bonança\r\n522087390110 - HUGO ARANTES -\r\n18825222815 & 5220', 'CANNABIS', 9000, 'MERCADO', 10000, 60, 5),
(5, 'PRODUTORA DE ARROZ DO SUL LTDA', 'https://cdn-icons-png.flaticon.com/512/5571/5571969.png', 'RUA QUALQUER', 'ARROZ', 3000, 'EXPORTAÇÃO', 159, 60, 5);

-- --------------------------------------------------------

--
-- Estrutura da tabela `taxation`
--

CREATE TABLE `taxation` (
  `taxation_id` int(11) NOT NULL,
  `taxation_name` varchar(30) NOT NULL,
  `taxation_valueprod` double NOT NULL,
  `taxation_IRPF` double NOT NULL,
  `taxation_CSLL` double NOT NULL,
  `taxation_PIS` double NOT NULL,
  `taxation_COFINS` double NOT NULL,
  `taxation_ICMS` double NOT NULL,
  `taxation_IPI` double NOT NULL,
  `taxation_INSS` double NOT NULL,
  `taxation_TOTAL` int(11) NOT NULL,
  `taxation_infID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `taxation`
--

INSERT INTO `taxation` (`taxation_id`, `taxation_name`, `taxation_valueprod`, `taxation_IRPF`, `taxation_CSLL`, `taxation_PIS`, `taxation_COFINS`, `taxation_ICMS`, `taxation_IPI`, `taxation_INSS`, `taxation_TOTAL`, `taxation_infID`) VALUES
(1, 'TRIBUTAÇÃO 25/10/2021', 500000, 6000, 60000, 6700, 15000, 60000, 75000, 27750, 222700, 1),
(2, 'TRIBUTAÇÃO 25/09/2021', 80000, 960, 9600, 0, 0, 0, 0, 4440, 10560, 5),
(3, 'TRIBUTAÇÃO 25/06/2021', 3000.75, 36.009, 360.09, 40.21005, 90.0225, 360.09, 450.1125, 166.54162499999998, 1337, 1),
(4, 'TRIBUTAÇÃO 26/06/2021', 10000, 120, 1200, 134, 300, 1200, 1500, 555, 4454, 1),
(5, 'TRIBUTAÇÃO 27/06/2021', 5000.55, 60.006600000000006, 600.066, 67.00737000000001, 150.0165, 600.066, 750.0825, 277.530525, 2227, 1),
(6, 'TRIBUTAÇÃO 28/06/2021', 8000.4, 96.00479999999999, 960.0479999999999, 107.20536, 240.01199999999997, 960.0479999999999, 1200.06, 444.02219999999994, 3563, 1),
(7, 'TRIBUTAÇÃO 29/06/2021', 9000.35, 108.0042, 1080.0420000000001, 120.60469, 270.01050000000004, 1080.0420000000001, 1350.0525, 499.51942499999996, 4009, 1),
(8, 'TRIBUTAÇÃO 30/06/2021', 15000.45, 180.0054, 1800.0540000000003, 201.00603000000004, 450.0135000000001, 1800.0540000000003, 2250.0675, 832.5249749999999, 6681, 1),
(9, 'TRIBUTAÇÃO 25/06/2021', 4000.75, 48.00899999999999, 480.09, 53.61005, 120.0225, 480.09, 600.1125, 222.04162499999998, 1782, 2),
(10, 'TRIBUTAÇÃO 26/06/2021', 70000, 840, 8400, 938, 2100, 8400, 10500, 3885, 31178, 2),
(11, 'TRIBUTAÇÃO 27/06/2021', 11000.55, 132.0066, 1320.0659999999998, 147.40737, 330.01649999999995, 1320.0659999999998, 1650.0825, 610.5305249999999, 4900, 2),
(12, 'TRIBUTAÇÃO 28/06/2021', 20000.4, 240.00480000000005, 2400.0480000000002, 268.00536000000005, 600.0120000000001, 2400.0480000000002, 3000.06, 1110.0222, 8908, 2),
(13, 'TRIBUTAÇÃO 29/06/2021', 30000.35, 360.00419999999997, 3600.0419999999995, 402.00469, 900.0104999999999, 3600.0419999999995, 4500.0525, 1665.0194249999997, 13362, 2),
(14, 'TRIBUTAÇÃO 30/06/2021', 44000.45, 528.0053999999999, 5280.053999999999, 589.60603, 1320.0134999999998, 5280.053999999999, 6600.0675, 2442.024975, 19598, 2),
(15, 'TRIBUTAÇÃO 25/06/2021', 500.75, 6.009000000000001, 60.09, 6.71005, 15.0225, 60.09, 75.1125, 27.791625, 223, 3),
(16, 'TRIBUTAÇÃO 26/06/2021', 900, 10.8, 108, 12.06, 27, 108, 135, 49.95, 401, 3),
(17, 'TRIBUTAÇÃO 27/06/2021', 5000.55, 60.006600000000006, 600.066, 67.00737000000001, 150.0165, 600.066, 750.0825, 277.530525, 2227, 3),
(18, 'TRIBUTAÇÃO 28/06/2021', 4000.4, 48.004799999999996, 480.048, 53.60536, 120.012, 480.048, 600.06, 222.0222, 1782, 3),
(19, 'TRIBUTAÇÃO 29/06/2021', 2000.35, 24.0042, 240.04199999999997, 26.80469, 60.01049999999999, 240.04199999999997, 300.0525, 111.019425, 891, 3),
(20, 'TRIBUTAÇÃO 30/06/2021', 800.45, 9.6054, 96.05400000000002, 10.726030000000002, 24.013500000000004, 96.05400000000002, 120.0675, 44.424975, 357, 3),
(21, 'TRIBUTAÇÃO 25/06/2021', 40000.75, 480.009, 4800.09, 536.0100500000001, 1200.0225, 4800.09, 6000.1125, 2220.041625, 17816, 4),
(22, 'TRIBUTAÇÃO 26/06/2021', 40000, 480, 4800, 536, 1200, 4800, 6000, 2220, 17816, 4),
(23, 'TRIBUTAÇÃO 27/06/2021', 45000.55, 540.0066, 5400.066000000001, 603.00737, 1350.0165000000002, 5400.066000000001, 6750.0825, 2497.530525, 20043, 4),
(24, 'TRIBUTAÇÃO 28/06/2021', 42000.4, 504.00480000000005, 5040.048000000001, 562.8053600000001, 1260.0120000000002, 5040.048000000001, 6300.06, 2331.0222, 18707, 4),
(25, 'TRIBUTAÇÃO 29/06/2021', 30000.35, 360.00419999999997, 3600.0419999999995, 402.00469, 900.0104999999999, 3600.0419999999995, 4500.0525, 1665.0194249999997, 13362, 4),
(26, 'TRIBUTAÇÃO 30/06/2021', 61000.45, 732.0054000000001, 7320.053999999999, 817.40603, 1830.0134999999998, 7320.053999999999, 9150.0675, 3385.524975, 27170, 4),
(27, 'TRIBUTAÇÃO 25/06/2021', 4000.75, 48.00899999999999, 480.09, 0, 0, 0, 0, 222.04162499999998, 528, 5),
(28, 'TRIBUTAÇÃO 26/06/2021', 900, 10.8, 108, 0, 0, 0, 0, 49.95, 119, 5),
(29, 'TRIBUTAÇÃO 27/06/2021', 2000.55, 24.0066, 240.06599999999997, 0, 0, 0, 0, 111.030525, 264, 5),
(30, 'TRIBUTAÇÃO 28/06/2021', 1800.4, 21.6048, 216.04800000000003, 0, 0, 0, 0, 99.92219999999999, 238, 5),
(31, 'TRIBUTAÇÃO 29/06/2021', 3050.35, 36.6042, 366.042, 0, 0, 0, 0, 169.29442499999996, 403, 5),
(32, 'TRIBUTAÇÃO 30/06/2021', 610.45, 7.325400000000001, 73.254, 0, 0, 0, 0, 33.879975, 81, 5);

-- --------------------------------------------------------

--
-- Estrutura da tabela `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nm_user` varchar(30) DEFAULT NULL,
  `pass_user` varchar(30) DEFAULT NULL,
  `picture_user` varchar(200) DEFAULT NULL,
  `user_reg` int(11) DEFAULT NULL,
  `user_role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `users`
--

INSERT INTO `users` (`id`, `nm_user`, `pass_user`, `picture_user`, `user_reg`, `user_role`) VALUES
(1, 'Jeferson Oliveira', '123456', NULL, 876978, 3),
(2, 'Jefferson Pereira', '1234', NULL, 876979, 2),
(3, 'Jorge Fernando', '123456', NULL, 876980, 3),
(4, 'PAULO GUEDES', '123456', NULL, 876981, 2),
(5, 'AQUINO SALLES', '123456', NULL, 876982, 3);

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `biohazard_pesticide`
--
ALTER TABLE `biohazard_pesticide`
  ADD PRIMARY KEY (`bio_id`);

--
-- Índices para tabela `inf_taxes`
--
ALTER TABLE `inf_taxes`
  ADD PRIMARY KEY (`taxe_id`);

--
-- Índices para tabela `job_role`
--
ALTER TABLE `job_role`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `private_infos`
--
ALTER TABLE `private_infos`
  ADD PRIMARY KEY (`inf_id`),
  ADD UNIQUE KEY `inf_name` (`inf_name`);

--
-- Índices para tabela `taxation`
--
ALTER TABLE `taxation`
  ADD PRIMARY KEY (`taxation_id`);

--
-- Índices para tabela `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `biohazard_pesticide`
--
ALTER TABLE `biohazard_pesticide`
  MODIFY `bio_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de tabela `inf_taxes`
--
ALTER TABLE `inf_taxes`
  MODIFY `taxe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de tabela `job_role`
--
ALTER TABLE `job_role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de tabela `private_infos`
--
ALTER TABLE `private_infos`
  MODIFY `inf_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de tabela `taxation`
--
ALTER TABLE `taxation`
  MODIFY `taxation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de tabela `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

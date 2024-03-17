package org.web.webauthorization.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.web.webauthorization.BankData.FinancialOperation.*;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankDataRepository.FinancialOperation.DepositRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.web.webauthorization.BankDataRepository.FinancialOperation.FinancialOperationRepository;
import org.web.webauthorization.BankDataRepository.FinancialOperation.TransactionRepository;
import org.web.webauthorization.BankDataRepository.Accounts.UserAccountRepository;
import org.web.webauthorization.Services.DepositService;
import org.web.webauthorization.Services.UserAccountService;

import static org.web.webauthorization.BankData.FinancialOperation.FinancialOperation.DepositActions.WITHDRAW;

@Controller
public class MainController {

    private final UserAccountRepository userAccountRepository;
    private final DepositRepository depositRepository;
    private final UserAccountService userAccountService;
    private final TransactionRepository transactionRepository;
    private final FinancialOperationRepository financialOperationRepository;
    private final DepositService depositService;

    UserAccount mainUser = null;

    @Autowired
    public MainController(UserAccountRepository userAccountRepository, TransactionRepository transactionRepository,
                          UserAccountService userAccountService, DepositRepository depositRepository,
                          FinancialOperationRepository financialOperationRepository, DepositService depositService) {
        this.userAccountRepository = userAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userAccountService = userAccountService;
        this.depositRepository = depositRepository;
        this.financialOperationRepository = financialOperationRepository;
        this.depositService = depositService;
    }

    @GetMapping("/deposits/{depositID}")
    @ResponseBody
    public Optional<Deposit> getDeposit(@PathVariable Long depositID) {
        System.out.println("zxc");
        Optional<Deposit> depositOptional = depositRepository.findById(depositID);
        System.out.println(depositOptional.get().getDepositID());
        System.out.println("zxc");

        return Optional.of(depositOptional.orElse(null));
    }


    @GetMapping("/operation-details/{operationId}")
    @ResponseBody
    public FinancialOperationDTO getOperationDetails(@PathVariable Long operationId, Authentication authentication) {
        Optional<FinancialOperation> operationOptional = financialOperationRepository.findById(operationId);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            mainUser = userAccountRepository.findByAccountName(username);
        }

        if (operationOptional.isPresent()) {
            FinancialOperation operation = operationOptional.get();
            FinancialOperationDTO dto = new FinancialOperationDTO();


            System.out.println("qwe");

            // Populate DTO fields from the operation entity
            dto.setOperationId(operation.getOperationId());
            dto.setOperationDate(operation.getOperationDate());
            dto.setAmount(operation.getAmount());
            dto.setSenderId(operation.getSenderId());
            dto.setRecipientId(operation.getRecipientId());
            dto.setOperationCreatorId(operation.getOperationCreatorId());

            if (operation instanceof Transaction transaction) {

                if (Objects.equals(operation.getSenderId(), mainUser.getId())) {
                    dto.setTransactionType(FinancialOperationDTO.TransactionType.SEND);
                } else {
                    dto.setTransactionType(FinancialOperationDTO.TransactionType.RECEIVED);
                }

                dto.setSender(transaction.getSender());
                dto.setRecipient(transaction.getRecipient());
                dto.setComment(transaction.getComment());
            } else if (operation instanceof DepositActions depositHistory) {

                if (operation.getDepositActions() == WITHDRAW) {
                    dto.setDepositActions(FinancialOperation.DepositActions.WITHDRAW);
                } else {
                    dto.setDepositActions(FinancialOperation.DepositActions.DEPOSIT);
                }
                // Set DepositHistory-specific fields
                dto.setUserBalanceBeforeOperation(depositHistory.getUserBalanceBeforeOperation());
                dto.setUserBalanceAfterOperation(depositHistory.getUserBalanceAfterOperation());
            }
            // Handle additional types as needed

            return dto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, STR."Operation not found with id: \{operationId}");
        }
    }


    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            mainUser = userAccountRepository.findByAccountName(username);

            UserAccount userAccount = userAccountRepository.findByAccountName(username);
            BigDecimal balance = userAccount.getAccountBalance();

            model.addAttribute("userBalance", balance);
        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {

            ArrayList<Deposit> deposits = depositRepository.findByOwnerID(mainUser.getId());
            model.addAttribute("deposits", deposits);

        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            UserAccount mainUser = userAccountRepository.findByAccountName(username);
            Long userId = mainUser.getId();

            List<FinancialOperation> operations = financialOperationRepository.findBySenderIdOrRecipientIdOrOperationCreatorId(userId, userId, userId);
            Collections.reverse(operations);

//            System.out.println("\n\n\n ------------------------------------------------------------------------------------------");
//            System.out.println(operations.size());
//            System.out.println("\n\n\n ------------------------------------------------------------------------------------------");

            List<FinancialOperationDTO> operationDTOs = operations.stream().map(operation -> {
                FinancialOperationDTO dto = new FinancialOperationDTO();

                dto.setOperationId(operation.getOperationId());
                dto.setOperationDate(operation.getOperationDate());
                dto.setAmount(operation.getAmount());
                dto.setSenderId(operation.getSenderId());
                dto.setRecipientId(operation.getRecipientId());
                dto.setOperationCreatorId(operation.getOperationCreatorId());

                if (operation instanceof Transaction transaction) {

                    if (Objects.equals(operation.getSenderId(), mainUser.getId())) {
                        dto.setTransactionType(FinancialOperationDTO.TransactionType.SEND);
                    } else {
                        dto.setTransactionType(FinancialOperationDTO.TransactionType.RECEIVED);
                    }
                    dto.setSender(transaction.getSender());
                    dto.setSenderBalanceBeforeTransaction(transaction.getSenderBalanceBeforeTransaction());
                    dto.setSenderBalanceAfterTransaction(transaction.getSenderBalanceAfterTransaction());
                    dto.setRecipient(transaction.getRecipient());
                    dto.setRecipientBalanceBeforeTransaction(transaction.getRecipientBalanceBeforeTransaction());
                    dto.setRecipientBalanceAfterTransaction(transaction.getRecipientBalanceAfterTransaction());
                    dto.setComment(transaction.getComment());
                } else if (operation instanceof DepositActions depositHistory) {

                    if (operation.getDepositActions() == WITHDRAW) {
                        dto.setDepositActions(FinancialOperation.DepositActions.WITHDRAW);
                    } else {
                        dto.setDepositActions(FinancialOperation.DepositActions.DEPOSIT);
                    }
                    dto.setUserBalanceBeforeOperation(depositHistory.getUserBalanceBeforeOperation());
                    dto.setUserBalanceAfterOperation(depositHistory.getUserBalanceAfterOperation());
                }
                return dto;
            }).collect(Collectors.toList());


            model.addAttribute("moneyHistory", operationDTOs);
        }
        return "main";
    }


    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity<Map<String, String>> transfer(
            @RequestParam("identifier") String identifier,
            @RequestParam("identifierType") String identifierType,
            @RequestParam("amount") BigDecimal transferSum,
            @RequestParam("comment") String comment) {

        String cardNumber = null;
        String recipientName = null;

        if ("cardNumber".equals(identifierType)) {
            cardNumber = identifier;
        } else if ("username".equals(identifierType)) {
            recipientName = identifier;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        UserAccount userRecipient = null;
        if (cardNumber != null) {
            String realCardNumber = UserAccount.removeSpaces(cardNumber);
            userRecipient = userAccountRepository.findByCardNumber(realCardNumber);
        } else {
            userRecipient = userAccountRepository.findByAccountName(recipientName);
        }
        UserAccount userSender = userAccountRepository.findByAccountName(currentPrincipalName);

        Map<String, String> response = new HashMap<>();
        String error = null;

        if (userRecipient == null) {
            error = "Recipient not found";
        } else if (userSender.getId().equals(userRecipient.getId())) {
            error = "Cannot transfer to the same account";
        } else if (transferSum.compareTo(BigDecimal.ZERO) <= 0) {
            error = "The amount must be greater than 0";
        } else if (transferSum.compareTo(userSender.getAccountBalance()) > 0) {
            error = "Insufficient funds";
        }

        if (error != null) {
            response.put("success", "false");
            response.put("message", error);
            return ResponseEntity.badRequest().body(response);
        }

        userAccountService.newTransfer(userSender.getId(), userRecipient.getId(), transferSum, comment);

        response.put("success", "true");
        response.put("message", "Transaction successful");
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/createDeposit")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createDeposit(@RequestParam("deposit-name") String depositName,
                                                             @RequestParam("deposit-amount") BigDecimal depositSum) {

        Map<String, String> response = new HashMap<>();
        String error = null;

        if (depositSum.compareTo(BigDecimal.ZERO) < 0) {
            error = "deposit sum can't be less than 0";
        }
        if (error != null) {
            response.put("success", "false");
            response.put("message", error);
            return ResponseEntity.badRequest().body(response);
        }

        Deposit deposit = new Deposit();
        deposit.setDepositName(depositName);
        deposit.setDepositGoalAmount(depositSum);
        deposit.setOwnerID(mainUser.getId());


        System.out.println(STR."\{deposit.getDepositID()}\n\{deposit.getOwnerID()}\n\{deposit.getDepositGoalAmount()}\n\{deposit.getDepositName()}");

        depositRepository.save(deposit);

        response.put("success", "true");
        response.put("message", "Transaction successful");
        return ResponseEntity.ok().body(response);
    }

}
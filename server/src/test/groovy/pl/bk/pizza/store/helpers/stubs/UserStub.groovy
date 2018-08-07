package pl.bk.pizza.store.helpers.stubs

import pl.bk.pizza.store.application.dto.user.NewUserDTO

class UserStub
{
    static NewUserDTO getNewUserDTOStub()
    {
        return new NewUserDTO(
            "aa@wp.pl",
            "pass",
            )
    }

    static NewUserDTO getNewUserDTOStub(String email)
    {
        return new NewUserDTO(
            email,
            "pass",
            )
    }
}

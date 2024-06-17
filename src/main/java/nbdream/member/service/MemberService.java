package nbdream.member.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.member.domain.Authority;
import nbdream.member.domain.Member;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean validateDuplicateNickname(final String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        return (member.isEmpty()) ? true : false;
    }

    public TokenResponse signup(String nickname) {
        if (!validateDuplicateNickname(nickname))
            throw new DuplicateRequestException();

        Member member = Member.builder()
                .socialId(UUID.randomUUID().toString())
                .authority(Authority.USER)
                .nickname(nickname)
                .build();

        Long id = memberRepository.save(member).getId();
        return new TokenResponse(jwtTokenProvider.createAccessToken(id), jwtTokenProvider.createRefreshToken(id));
    }
}

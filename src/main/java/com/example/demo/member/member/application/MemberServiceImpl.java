package com.example.demo.member.member.application;

import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.boardscore.domain.BoardScoreRepository;
import com.example.demo.config.util.methodtimer.MethodTimer;
import com.example.demo.config.exception.DuplicatedException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardScoreRepository boardScoreRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.save()")
    public void save(MemberRequest.SaveMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.SaveMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.deleteByUserId")
    public void deleteByUserId(Long takenUserId) {
        Member saveEntity = memberRepository.findById(takenUserId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        boardScoreRepository.deleteByMember(saveEntity);
        boardRepository.deleteByMember(saveEntity);
        memberRepository.deleteById(takenUserId);
    }

    @Override
    @MethodTimer(method = "MemberService.findById()")
    public MemberResponse.FindMember findById(Long takenUserId) {
        Member savedEntity = memberRepository.findById(takenUserId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return MemberResponse.FindMember.toDto(savedEntity);
    }

    @Override
    @MethodTimer(method = "MemberService.findByIdDetail()")
    public MemberResponse.FindMemberDetail findByIdDetail(Long takenUserId) {
        Member savedEntity = memberRepository.findById(takenUserId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return MemberResponse.FindMemberDetail.toDto(savedEntity);
    }

    @Override
    @MethodTimer(method = "MemberService.createNewPassword()")
    public String createNewPassword(String takenUsername, String takenEmail) {
        Member savedEntity = memberRepository.findByUsername(takenUsername)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        if(!savedEntity.getEmail().equals(takenEmail))
            throw new NotFoundDataException("해당 유저의 이메일을 찾지 못했습니다");

        String newPassword = this.createNewPassword();
        this.patchPasswordByUsername(savedEntity.getId(), newPassword);

        return newPassword;
    }

    private String createNewPassword() {
        String password = "";
        for(int i = 0; i < 4; i++) {
            password += (char) ((int) (Math.random() * 26) + 97);
        }
        for(int i = 0; i < 4; i++) {
            password += (int) (Math.random() * 10);
        }
        return password;
    }

    @Override
    @MethodTimer(method = "MemberService.findUsernameByEmail()")
    public String findUsernameByEmail(String takenEmail) {
        Member savedEntity = memberRepository.findByEmail(takenEmail)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return savedEntity.getUsername();
    }

    @Override
    @MethodTimer(method = "MemberService.checkDuplicated()")
    public void checkDuplicated(MemberRequest.DataConfirmMember takenDataConfirmMember) {
        String takenNickname = takenDataConfirmMember.nickname();
        String takenUsername = takenDataConfirmMember.username();

        if(memberRepository.existsByNickname(takenNickname))
            throw new DuplicatedException("중복된 닉네임입니다");

        if(memberRepository.existsByUsername(takenUsername))
            throw new DuplicatedException("중복된 아이디입니다");
    }

    @Override
    @MethodTimer(method = "MemberService.checkDuplicatedByEmail()")
    public void checkDuplicatedByEmail(String takenEmail) {
        if(memberRepository.existsByEmail(takenEmail))
            throw new DuplicatedException("중복된 이메일입니다");
    }

    @Override
    @MethodTimer(method = "MemberService.patchNicknameById()")
    public void patchNicknameById(Long takenId, String takenNickname) {
        Member savedEntity = memberRepository.findById(takenId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        savedEntity.patchNickname(takenNickname);
        memberRepository.save(savedEntity);
    }

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.patchPasswordByUsername()")
    public void patchPasswordByUsername(Long takenId, String takenPassword) {
        Member savedEntity = memberRepository.findById(takenId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        savedEntity.patchPassword(passwordEncoder.encode(takenPassword));
        memberRepository.save(savedEntity);
    }
}

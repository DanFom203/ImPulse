<script setup>
import MainLayout from "@/components/blocks/MainLayout.vue"
import UserRoleForm from '@/components/forms/UserRoleForm.vue'
import UserSettingsForm from '@/components/forms/UserSettingsForm.vue'
import ProfileForm from '@/components/forms/ProfileForm.vue'
</script>

<template>
  <MainLayout>
    <div class="profile-layout">
      <UserRoleForm />
      <ProfileForm />
      <UserSettingsForm />
    </div>
  </MainLayout>
</template>

<style src="@/assets/profile.css"></style>

<template>
	<div class="app-container">
		<s-card-schule-datenaustausch-kurs42 :set-gost-kurs42-import-zip="setGostKurs42ImportZip" />
	</div>
</template>

<script setup lang="ts">

	const props = defineProps<{
		setGostKurs42ImportZip: (formData: FormData) => Promise<boolean>;
	}>();

</script>

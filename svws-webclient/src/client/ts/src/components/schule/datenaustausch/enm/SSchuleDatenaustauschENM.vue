<template>
	<div class="app-container">
		<s-card-schule-datenaustausch-e-n-m :set-import-e-n-m="setImportENM" />
	</div>
</template>

<script setup lang="ts">

	const props = defineProps<{
		setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
	}>();

</script>

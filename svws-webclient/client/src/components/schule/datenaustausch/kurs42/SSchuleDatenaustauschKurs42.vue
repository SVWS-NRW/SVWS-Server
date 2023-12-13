<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-kurs42 :set-gost-kurs42-import-zip="setGostKurs42ImportZip" />
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Import aus Kurs42</span>
		<br>
		<span class="opacity-50"><i-ri-download2-line /></span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	const props = defineProps<{
		setGostKurs42ImportZip: (formData: FormData) => Promise<boolean>;
	}>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>

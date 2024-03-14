<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-e-n-m :set-import-e-n-m="setImportENM" />
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Datenaustausch mit dem Externen Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="icon i-ri-download-2-line icon-xl" />
			<span class="icon i-ri-upload-2-line icon-xl" />
		</span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';

	const props = defineProps<{
		setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
	}>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>

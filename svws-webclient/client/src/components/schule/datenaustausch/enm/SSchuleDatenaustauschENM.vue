<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch mit dem Externen Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page page-flex-row">
				<s-card-schule-datenaustausch-e-n-m :set-import-e-n-m="setImportENM" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	const props = defineProps<{
		setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
	}>();

</script>

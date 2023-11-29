<template>
	<svws-ui-app-layout fullwidth-content class="app--layout--login">
		<template #main>
			<div class="login-wrapper">
				<div class="login-container pt-5">
					<div class="login-form modal modal--sm my-auto">
						<div class="modal--content-wrapper pb-3">
							<div class="modal--content px-5">
								<div class="mb-6 mt-2">
									<h1 class="font-bold text-headline-sm leading-none w-full py-2">
										Laufbahnplanung Oberstufe - Datei laden
									</h1>
								</div>
								<svws-ui-input-wrapper center>
									<input type="file" accept=".lp" @change="import_file" :disabled="loading">
									<svws-ui-spinner :spinning="loading" />
									<br> {{ status === false ? "Fehler beim Import" : status === true ? "Import erfolgreich" : "" }}
								</svws-ui-input-wrapper>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { LadeDatenProps } from "./LadeDatenProps";

	const props = defineProps<LadeDatenProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		await props.importLaufbahnplanung(formData);
		status.value = true;
		loading.value = false;
	}

</script>

<style lang="postcss" scoped>
.login-wrapper {
	@apply flex h-full flex-col justify-between;
}

.app--layout--login {
	@apply p-0 bg-none bg-transparent;
}

.app--layout--login :global(.app--content-container) {
	@apply bg-white/5;
}

.login-container {
	@apply bg-cover bg-top h-full flex flex-col justify-center items-center px-4;
	/*background-image: url('/images/noise.svg'), url('/images/placeholder-background.jpg');
	background-size: 100px, cover;
	background-blend-mode: overlay, normal;*/
	background-image: url('/images/placeholder-background.jpg');
	/*background: radial-gradient(circle at 50% 50%, rgba(255, 255, 255, 0.8), transparent 90%),
	linear-gradient(to top, #2285d5 0%, transparent 70%),
	linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.4) 70%),
	#e3eefb;
	animation: bg 30s infinite;

	&:before {
		content: '';
		@apply absolute inset-0 pointer-events-none;
		background: radial-gradient(circle at 20% 20%, rgba(255, 255, 255, 0.5), transparent 60%);
	}

	&:after {
		content: '';
		@apply absolute inset-0 opacity-10 pointer-events-none;
    background-image:  linear-gradient(rgba(255, 255, 255, 1) 2px, transparent 2px), linear-gradient(90deg, rgba(255, 255, 255, 1) 2px, transparent 2px), linear-gradient(rgba(255, 255, 255, 1) 1px, transparent 1px), linear-gradient(90deg, rgba(255, 255, 255, 1) 1px, rgba(255, 255, 255, 0) 1px);
    background-size: 50px 50px, 50px 50px, 10px 10px, 10px 10px;
    background-position: -2px -2px, -2px -2px, -1px -1px, -1px -1px;
	}*/
}

@keyframes bg {
	0%, 100% { background-color: #2285d5; }
  25% { background-color: #8a5cf6; }
  50% { background-color: #84cc16; }
  75% { background-color: #fff693; }
}

.modal {
	@apply shadow-2xl shadow-black/50 rounded-3xl;
}

</style>

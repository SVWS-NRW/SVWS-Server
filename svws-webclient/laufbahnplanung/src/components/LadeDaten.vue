<template>
	<svws-ui-app-layout fullwidth-content class="app--layout--login">
		<template #main>
			<div class="flex h-full flex-col justify-between">
				<div class="bg-cover bg-top h-full flex flex-col justify-center items-center px-4 pt-5 bg-[url(@/images/start-hintergrund.jpg)]">
					<div class="login-form modal modal--sm my-auto">
						<div class="modal--content-wrapper pb-3">
							<div class="modal--content px-5">
								<div class="mb-6 mt-2">
									<h1 class="font-bold text-headline-xl leading-none w-full py-2">
										SVWS NRW
									</h1>
									<h2 class="text-headline-sm leading-tight opacity-50">Schulverwaltung für<br>Nordrhein-Westfalen</h2>
									<h1 class="font-bold text-headline-sm leading-none w-full py-2 mt-3">
										Laufbahnplanung Oberstufe - Datei laden
									</h1>
								</div>
								<svws-ui-input-wrapper center>
									<input type="file" accept=".lp" @change="import_file" :disabled="loading">
									<svws-ui-spinner :spinning="loading" />
									<br> {{ (typeof status === "string") ? ("Fehler beim Import: " + status) : ((status === null) ? "Import erfolgreich" : "") }}
								</svws-ui-input-wrapper>
								<div class="mt-3 -mb-3 opacity-50">
									<p class="text-sm text-left">
										Bitte beachten Sie, dass keinerlei personenbezogenen Daten
										auf unsere Server geladen werden. Die Daten verbleiben auf Ihrem Endgerät.
									</p>
								</div>
								<div class="mt-16 text-sm font-medium">
									<div class="flex gap-3 items-center opacity-50">
										<img src="/images/Wappenzeichen_NRW_bw.svg" alt="" class="h-8">
										<div class="text-left">
											<p>
												Powered by SVWS-NRW <span class="font-bold">Version {{ version }}</span>
											</p>
											<nav class="flex flex-row items-center gap-2 mt-0.5">
												<a class="login-footer-link" href="#">Impressum</a>
												<a class="login-footer-link" href="#">Datenschutz</a>
											</nav>
										</div>
									</div>
									<div class="mt-3 -mb-3 opacity-50">
										<p class="text-sm text-left">
											Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf
											geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw.
											zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle
											Geschlechter gleichermaßen zu berücksichtigen.
										</p>
									</div>
								</div>
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
	import { version } from '../../version';

	const props = defineProps<LadeDatenProps>();

	const status = ref<string | null | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		status.value = await props.importLaufbahnplanung(formData);
		loading.value = false;
	}

</script>

<style lang="postcss" scoped>

	@reference "../../../ui/src/assets/styles/index.css";

	.app--layout--login {
		@apply p-0 bg-none bg-transparent;
	}

	.modal {
		@apply shadow-2xl shadow-black/50 rounded-3xl;
	}

	input[type="file" i]{ /* the name of the selected file */
		@apply w-full;
	}

</style>

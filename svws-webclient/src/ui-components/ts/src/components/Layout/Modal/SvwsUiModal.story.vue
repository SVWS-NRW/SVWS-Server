<script setup lang="ts">
	import { ref } from 'vue';
	import { hstEvent } from 'histoire/client';

	const modal = ref<any>(null);
	const modalDanger = ref<any>(null);

	function onClick(event: Event) {
		hstEvent('click', event);
	}
</script>

<template>
	<Story title="SVWS UI/Layout/Modal">
		<div class="flex gap-2">
			<svws-ui-button @click="modal.openModal()">Open Modal</svws-ui-button>
			<svws-ui-button @click="modalDanger.openModal()" type="danger">Dangerous Action</svws-ui-button>
		</div>

		<svws-ui-modal ref="modal" size="small">
			<template #modalTitle>Schüler hinzufügen</template>
			<template #modalDescription>
				Welches Schuljahr soll bei den neu aufzunehmenden Schülern gesetzt
				werden?
			</template>
			<template #modalContent>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option value="option3" name="radioInputExample" label="Vergangenes Schuljahr 2019/2020"
						disabled />
					<svws-ui-radio-option value="option1" name="radioInputExample" label="Aktuelles Schuljahr 2020/21" />
					<svws-ui-radio-option value="option2" name="radioInputExample" label="Kommendes Schuljahr 2021/22" checked />
				</svws-ui-radio-group>
				<p class="hidden">
					<span>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorem earum eligendi fugiat harum incidunt ipsa, molestiae perspiciatis porro quis reprehenderit. Adipisci est id impedit nihil officia quia suscipit, vitae voluptatem.</span><span>Assumenda autem consectetur consequuntur eum eveniet exercitationem impedit in magnam nam neque officia, perspiciatis provident quisquam repellendus saepe sint, voluptas? Architecto atque delectus enim magni perferendis placeat possimus provident vitae!</span><span>A assumenda consequuntur doloribus earum, error id ipsam libero molestias nam neque, nesciunt quo ratione similique temporibus voluptatibus. Earum eius fuga nihil officia quisquam? Blanditiis delectus doloribus perferendis vel voluptas!</span><span>Alias asperiores aspernatur aut cumque distinctio ducimus magni pariatur perspiciatis quia? Commodi dicta sed voluptates! Ad aliquam cumque dolorem exercitationem illo inventore ipsa iste iure mollitia, pariatur, praesentium vero voluptates!</span><span>Ab architecto at corporis cumque dolorum ea eius est ex explicabo hic ipsam iste labore minus natus optio praesentium, quia quo rem sapiente sit suscipit tempora totam! Blanditiis, harum, provident.</span><span>Accusantium assumenda at beatae commodi deleniti eaque enim fuga hic illum impedit incidunt ipsum labore minima natus neque obcaecati, odio odit omnis perspiciatis quisquam quod rerum tempore ullam vitae, voluptatum!</span><span>Asperiores culpa, doloremque dolores earum eius et exercitationem facere harum hic illo nesciunt, nobis non odit quae sequi vero vitae voluptates, voluptatibus. Dicta, doloribus voluptatem. Fuga libero quam vero voluptatem?</span><span>Aliquid asperiores commodi culpa debitis dicta doloribus earum eligendi est id inventore ipsam magni maxime mollitia nam nesciunt nisi quaerat quia recusandae repellendus totam, ullam ut voluptates! Asperiores officiis, velit!</span><span>Assumenda, corporis debitis dicta dignissimos dolore, doloremque eos eveniet exercitationem explicabo incidunt modi pariatur quaerat ratione repellat sed soluta unde! Consequatur earum eligendi ipsa obcaecati odio saepe tenetur velit. Distinctio!</span><span>Accusantium ad alias animi aut consectetur consequuntur, delectus ducimus eaque et ex explicabo harum ipsa iusto libero magni natus odit officia pariatur praesentium quasi quidem ratione rem rerum sit voluptate.</span><span>Accusamus asperiores, at autem cupiditate deserunt dolore dolorem doloremque eaque exercitationem facilis, impedit magnam maxime molestiae nostrum omnis perferendis, quisquam quos repellat temporibus ullam unde vitae voluptas voluptates. Ea, non.</span><span>Ab architecto consequuntur dignissimos dolore in officia quo tempore? Amet aspernatur aut beatae corporis debitis ea fugiat illo, iure maiores nisi nobis nostrum, possimus rerum saepe sed sit ullam vel?</span><span>Alias consectetur consequatur consequuntur eveniet fuga hic incidunt ipsam necessitatibus quidem sed. Autem, cumque eos eveniet, exercitationem expedita facere incidunt iste maiores minus molestiae natus sapiente sit tenetur voluptates voluptatibus.</span><span>Ab aliquam autem fuga odio optio quod vel veniam voluptatem voluptates! Accusantium alias aliquid delectus dicta distinctio eligendi est et ex modi molestias perferendis possimus praesentium provident quia, voluptas voluptatibus!</span><span>Cumque deleniti distinctio et eveniet impedit itaque non quam quo tempore! Ad, aliquid asperiores culpa doloremque ducimus esse fugiat inventore omnis, porro, possimus praesentium quasi quidem quisquam recusandae vitae voluptatum!</span><span>Aperiam aut corporis, culpa cupiditate deleniti excepturi facere facilis fugit illo ipsa iusto magnam, molestiae mollitia nostrum perferendis provident quos, ut vel voluptas voluptatibus? Aspernatur atque natus praesentium quod repellat.</span><span>Ea laudantium minima neque odit possimus quo sit unde veritatis vero voluptatem. Asperiores beatae deleniti eligendi eos est ex excepturi facilis nemo, praesentium recusandae? Eius ipsum molestias nulla quibusdam totam?</span><span>Asperiores at beatae dignissimos, distinctio doloribus eveniet ex ipsa libero, magni minus, modi nemo quam quas quasi quia reiciendis unde voluptatem? Alias consectetur culpa dolorem dolores ipsam magni odit, placeat.</span><span>Ad assumenda at, atque beatae corporis cum delectus deserunt distinctio ea, excepturi expedita explicabo harum incidunt iste iusto libero minima minus mollitia nesciunt qui reiciendis, rem sequi tempora tenetur voluptatum.</span><span>Consequatur ea, iusto laborum officia soluta temporibus ut veritatis. Distinctio dolorum enim error fuga iure laudantium non recusandae veritatis! A aliquid assumenda cumque eligendi facere fugit hic mollitia nihil rem?</span>
				</p>
			</template>

			<template #modalActions>
				<svws-ui-button type="secondary" @click="modal?.closeModal()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="onClick">Weiter</svws-ui-button>
			</template>
		</svws-ui-modal>

		<svws-ui-modal ref="modalDanger" size="small" type="danger">
			<template #modalTitle>Schüler hinzufügen</template>
			<template #modalDescription>
				Welches Schuljahr soll bei den neu aufzunehmenden Schülern gesetzt
				werden?
			</template>
			<template #modalContent>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option value="option3" name="radioInputExample" label="Vergangenes Schuljahr 2019/2020"
						disabled />
					<svws-ui-radio-option value="option1" name="radioInputExample" label="Aktuelles Schuljahr 2020/21" />
					<svws-ui-radio-option value="option2" name="radioInputExample" label="Kommendes Schuljahr 2021/22" checked />
				</svws-ui-radio-group>
				<p class="hidden">
					<span>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorem earum eligendi fugiat harum incidunt ipsa, molestiae perspiciatis porro quis reprehenderit. Adipisci est id impedit nihil officia quia suscipit, vitae voluptatem.</span><span>Assumenda autem consectetur consequuntur eum eveniet exercitationem impedit in magnam nam neque officia, perspiciatis provident quisquam repellendus saepe sint, voluptas? Architecto atque delectus enim magni perferendis placeat possimus provident vitae!</span><span>A assumenda consequuntur doloribus earum, error id ipsam libero molestias nam neque, nesciunt quo ratione similique temporibus voluptatibus. Earum eius fuga nihil officia quisquam? Blanditiis delectus doloribus perferendis vel voluptas!</span><span>Alias asperiores aspernatur aut cumque distinctio ducimus magni pariatur perspiciatis quia? Commodi dicta sed voluptates! Ad aliquam cumque dolorem exercitationem illo inventore ipsa iste iure mollitia, pariatur, praesentium vero voluptates!</span><span>Ab architecto at corporis cumque dolorum ea eius est ex explicabo hic ipsam iste labore minus natus optio praesentium, quia quo rem sapiente sit suscipit tempora totam! Blanditiis, harum, provident.</span><span>Accusantium assumenda at beatae commodi deleniti eaque enim fuga hic illum impedit incidunt ipsum labore minima natus neque obcaecati, odio odit omnis perspiciatis quisquam quod rerum tempore ullam vitae, voluptatum!</span><span>Asperiores culpa, doloremque dolores earum eius et exercitationem facere harum hic illo nesciunt, nobis non odit quae sequi vero vitae voluptates, voluptatibus. Dicta, doloribus voluptatem. Fuga libero quam vero voluptatem?</span><span>Aliquid asperiores commodi culpa debitis dicta doloribus earum eligendi est id inventore ipsam magni maxime mollitia nam nesciunt nisi quaerat quia recusandae repellendus totam, ullam ut voluptates! Asperiores officiis, velit!</span><span>Assumenda, corporis debitis dicta dignissimos dolore, doloremque eos eveniet exercitationem explicabo incidunt modi pariatur quaerat ratione repellat sed soluta unde! Consequatur earum eligendi ipsa obcaecati odio saepe tenetur velit. Distinctio!</span><span>Accusantium ad alias animi aut consectetur consequuntur, delectus ducimus eaque et ex explicabo harum ipsa iusto libero magni natus odit officia pariatur praesentium quasi quidem ratione rem rerum sit voluptate.</span><span>Accusamus asperiores, at autem cupiditate deserunt dolore dolorem doloremque eaque exercitationem facilis, impedit magnam maxime molestiae nostrum omnis perferendis, quisquam quos repellat temporibus ullam unde vitae voluptas voluptates. Ea, non.</span><span>Ab architecto consequuntur dignissimos dolore in officia quo tempore? Amet aspernatur aut beatae corporis debitis ea fugiat illo, iure maiores nisi nobis nostrum, possimus rerum saepe sed sit ullam vel?</span><span>Alias consectetur consequatur consequuntur eveniet fuga hic incidunt ipsam necessitatibus quidem sed. Autem, cumque eos eveniet, exercitationem expedita facere incidunt iste maiores minus molestiae natus sapiente sit tenetur voluptates voluptatibus.</span><span>Ab aliquam autem fuga odio optio quod vel veniam voluptatem voluptates! Accusantium alias aliquid delectus dicta distinctio eligendi est et ex modi molestias perferendis possimus praesentium provident quia, voluptas voluptatibus!</span><span>Cumque deleniti distinctio et eveniet impedit itaque non quam quo tempore! Ad, aliquid asperiores culpa doloremque ducimus esse fugiat inventore omnis, porro, possimus praesentium quasi quidem quisquam recusandae vitae voluptatum!</span><span>Aperiam aut corporis, culpa cupiditate deleniti excepturi facere facilis fugit illo ipsa iusto magnam, molestiae mollitia nostrum perferendis provident quos, ut vel voluptas voluptatibus? Aspernatur atque natus praesentium quod repellat.</span><span>Ea laudantium minima neque odit possimus quo sit unde veritatis vero voluptatem. Asperiores beatae deleniti eligendi eos est ex excepturi facilis nemo, praesentium recusandae? Eius ipsum molestias nulla quibusdam totam?</span><span>Asperiores at beatae dignissimos, distinctio doloribus eveniet ex ipsa libero, magni minus, modi nemo quam quas quasi quia reiciendis unde voluptatem? Alias consectetur culpa dolorem dolores ipsam magni odit, placeat.</span><span>Ad assumenda at, atque beatae corporis cum delectus deserunt distinctio ea, excepturi expedita explicabo harum incidunt iste iusto libero minima minus mollitia nesciunt qui reiciendis, rem sequi tempora tenetur voluptatum.</span><span>Consequatur ea, iusto laborum officia soluta temporibus ut veritatis. Distinctio dolorum enim error fuga iure laudantium non recusandae veritatis! A aliquid assumenda cumque eligendi facere fugit hic mollitia nihil rem?</span>
				</p>
			</template>

			<template #modalActions>
				<svws-ui-button type="secondary" @click="modal?.closeModal()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="onClick">Weiter</svws-ui-button>
			</template>
		</svws-ui-modal>
	</Story>
</template>
